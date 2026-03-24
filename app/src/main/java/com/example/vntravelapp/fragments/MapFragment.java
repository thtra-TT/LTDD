package com.example.vntravelapp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.Hotel;
import com.example.vntravelapp.models.MapItem;
import com.example.vntravelapp.R;
import com.example.vntravelapp.models.Tour;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapFragment extends Fragment {

    private static final String MAP_ASSET_URL = "file:///android_asset/vietnam-map-osm.html";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1102;
    private static final long LOCATION_REFRESH_COOLDOWN_MS = 15_000L;

    private WebView webMap;
    private ProgressBar pbLoading;
    private TextView tvError;
    private TextView tvNearestHighlight;

    private DatabaseHelper dbHelper;
    private FusedLocationProviderClient fusedLocationClient;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final List<MapItem> mapItems = new ArrayList<>();
    private final Map<String, MapItem> mapItemById = new HashMap<>();
    private boolean pageReady;
    private Location currentUserLocation;
    private String lastItemsPayload = "";
    private long lastLocationRequestAtMs = 0L;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        webMap = view.findViewById(R.id.webMap);
        pbLoading = view.findViewById(R.id.pbMapLoading);
        tvError = view.findViewById(R.id.tvMapError);
        tvNearestHighlight = view.findViewById(R.id.tvNearestHighlight);

        dbHelper = new DatabaseHelper(requireContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        configureWebView();
        loadMapPage();

        view.findViewById(R.id.btnReloadMap).setOnClickListener(v -> {
            refreshMapItemsFromDatabase();
            pushAllDataToWeb();
        });
        view.findViewById(R.id.fabMyLocation).setOnClickListener(v -> requestLocationPermissionAndFetch());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshMapItemsFromDatabase();
        pushAllDataToWeb();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void configureWebView() {
        if (webMap == null) {
            return;
        }
        WebView.setWebContentsDebuggingEnabled(true);
        webMap.setBackgroundColor(Color.parseColor("#0F172A"));

        WebSettings settings = webMap.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        webMap.addJavascriptInterface(new MapJsBridge(), "AndroidMapBridge");
        webMap.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
                String message = consoleMessage.message();
                if (message != null) {
                    String full = "JS " + consoleMessage.messageLevel() + " ["
                            + consoleMessage.sourceId() + ":" + consoleMessage.lineNumber() + "] " + message;
                    if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.ERROR
                            || consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.WARNING) {
                        showError(full);
                    }
                }
                return super.onConsoleMessage(consoleMessage);
            }
        });
        webMap.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pageReady = false;
                showLoading(true);
                showError(null);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pageReady = true;
                showLoading(false);
                refreshMapItemsFromDatabase();
                pushAllDataToWeb();
                if (currentUserLocation == null) {
                    requestLocationPermissionAndFetch();
                } else {
                    pushUserLocationToWeb();
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request == null) {
                    return;
                }
                String url = request.getUrl() == null ? "" : request.getUrl().toString();
                CharSequence reason = error == null ? "Khong the tai ban do" : error.getDescription();

                if (request.isForMainFrame()) {
                    showLoading(false);
                    showError("Khong tai duoc ban do: " + reason);
                    return;
                }

                if (url.contains("leaflet") || url.contains("openstreetmap") || url.contains("unpkg")) {
                    showError("Tai tai nguyen map that bai: " + reason + " | " + url);
                }
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, android.webkit.WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                if (request == null || errorResponse == null) {
                    return;
                }
                String url = request.getUrl() == null ? "" : request.getUrl().toString();
                if (url.contains("leaflet") || url.contains("openstreetmap") || url.contains("unpkg")) {
                    showError("Tai tai nguyen map that bai (HTTP " + errorResponse.getStatusCode() + "): " + url);
                }
            }
        });
    }

    private void loadMapPage() {
        if (webMap == null) {
            return;
        }
        showError(null);
        showLoading(true);
        webMap.loadUrl(MAP_ASSET_URL);
    }

    private void refreshMapItemsFromDatabase() {
        mapItems.clear();
        mapItemById.clear();
        mapItems.addAll(dbHelper.getAllMapItems());
        for (MapItem item : mapItems) {
            mapItemById.put(item.getId(), item);
        }
    }

    private void pushAllDataToWeb() {
        if (!pageReady || webMap == null) {
            return;
        }
        try {
            JSONArray itemsJson = new JSONArray();
            for (MapItem item : mapItems) {
                if (!isValidCoordinate(item.getLatitude(), item.getLongitude())) {
                    continue;
                }
                JSONObject object = new JSONObject();
                object.put("id", item.getId());
                object.put("kind", item.getKind() == MapItem.Kind.TOUR ? "tour" : "hotel");
                object.put("name", safe(item.getTitle()));
                object.put("price", safe(item.getPrice()));
                object.put("description", safe(item.getDescription()));
                object.put("thumbnail", safe(item.getThumbnailUrl()));
                object.put("lat", item.getLatitude());
                object.put("lon", item.getLongitude());
                itemsJson.put(object);
            }
            String payload = itemsJson.toString();
            if (!payload.equals(lastItemsPayload)) {
                lastItemsPayload = payload;
                evaluateJs("window.MapApp && window.MapApp.renderDbItems(" + payload + ");");
            }
            pushUserLocationToWeb();
        } catch (Exception e) {
            showError("Khong the dong bo du lieu ban do: " + e.getMessage());
        }
    }

    private void requestLocationPermissionAndFetch() {
        long now = System.currentTimeMillis();
        if (now - lastLocationRequestAtMs < LOCATION_REFRESH_COOLDOWN_MS) {
            return;
        }

        if (!hasLocationPermission()) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
            return;
        }
        lastLocationRequestAtMs = now;
        fetchCurrentLocation();
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    private void fetchCurrentLocation() {
        if (!hasLocationPermission()) {
            return;
        }
        CurrentLocationRequest request = new CurrentLocationRequest.Builder()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build();
        CancellationTokenSource cts = new CancellationTokenSource();

        try {
            fusedLocationClient.getCurrentLocation(request, cts.getToken())
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            onUserLocationResolved(location);
                            return;
                        }
                        fallbackLastLocation();
                    })
                    .addOnFailureListener(e -> fallbackLastLocation());
        } catch (SecurityException ignored) {
            // Permission can be revoked while request is in flight.
        }
    }

    @SuppressLint("MissingPermission")
    private void fallbackLastLocation() {
        if (!hasLocationPermission()) {
            return;
        }
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            onUserLocationResolved(location);
                        }
                    });
        } catch (SecurityException ignored) {
            // Permission can be revoked while request is in flight.
        }
    }

    private void onUserLocationResolved(@NonNull Location location) {
        currentUserLocation = location;
        pushUserLocationToWeb();
        highlightNearestTour();
    }

    private void pushUserLocationToWeb() {
        if (!pageReady || webMap == null || currentUserLocation == null) {
            return;
        }
        String script = String.format(
                Locale.US,
                "window.MapApp && window.MapApp.updateUserLocation(%f, %f);",
                currentUserLocation.getLatitude(),
                currentUserLocation.getLongitude()
        );
        evaluateJs(script);
    }

    private void highlightNearestTour() {
        if (currentUserLocation == null || mapItems.isEmpty()) {
            if (tvNearestHighlight != null) {
                tvNearestHighlight.setVisibility(View.GONE);
            }
            return;
        }

        MapItem nearestTour = null;
        double nearestDistance = Double.MAX_VALUE;
        for (MapItem item : mapItems) {
            if (item.getKind() != MapItem.Kind.TOUR || !isValidCoordinate(item.getLatitude(), item.getLongitude())) {
                continue;
            }
            double distance = distanceMeters(
                    currentUserLocation.getLatitude(),
                    currentUserLocation.getLongitude(),
                    item.getLatitude(),
                    item.getLongitude()
            );
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestTour = item;
            }
        }

        if (nearestTour == null) {
            if (tvNearestHighlight != null) {
                tvNearestHighlight.setVisibility(View.GONE);
            }
            return;
        }

        evaluateJs("window.MapApp && window.MapApp.highlightNearestTour(" + JSONObject.quote(nearestTour.getId()) + ");");

        if (tvNearestHighlight != null) {
            String distanceText = nearestDistance < 1000
                    ? ((int) nearestDistance) + " m"
                    : String.format(Locale.getDefault(), "%.1f km", nearestDistance / 1000.0);
            tvNearestHighlight.setText("Tour gan nhat: " + safe(nearestTour.getTitle()) + " - " + distanceText);
            tvNearestHighlight.setVisibility(View.VISIBLE);
        }
    }

    private void evaluateJs(@NonNull String script) {
        mainHandler.post(() -> {
            if (webMap != null) {
                webMap.evaluateJavascript(script, null);
            }
        });
    }

    private void openItemDetail(@Nullable String itemId) {
        if (itemId == null || itemId.trim().isEmpty()) {
            return;
        }
        MapItem item = mapItemById.get(itemId);
        if (item == null) {
            return;
        }
        if (item.getTour() != null) {
            openTourDetail(item.getTour());
            return;
        }
        if (item.getHotel() != null) {
            openHotelDetail(item.getHotel());
        }
    }

    private void openTourDetail(@NonNull Tour tour) {
        String primaryImageUrl = tour.getPrimaryImageUrl();
        DetailFragment fragment = DetailFragment.newInstance(
                tour.getTitle(),
                tour.getLocation(),
                tour.getPrice(),
                tour.getDescription(),
                tour.getItinerary(),
                tour.getIncluded(),
                tour.getExcluded(),
                tour.getImageResId(),
                primaryImageUrl,
                new ArrayList<>(tour.getImageUrls()),
                tour.getVideoUrl(),
                tour.getRating(),
                tour.getReviewCount()
        );
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void openHotelDetail(@NonNull Hotel hotel) {
        ArrayList<String> imageUrls = new ArrayList<>();
        if (hotel.getImageUrl() != null && !hotel.getImageUrl().trim().isEmpty()) {
            imageUrls.add(hotel.getImageUrl().trim());
        }

        DetailFragment fragment = DetailFragment.newInstance(
                hotel.getName(),
                hotel.getLocation(),
                hotel.getPrice(),
                hotel.getDescription(),
                "",
                "",
                "",
                hotel.getImageRes(),
                hotel.getImageUrl(),
                imageUrls,
                null,
                hotel.getRating(),
                hotel.getReviewCount()
        );
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private boolean isValidCoordinate(double lat, double lon) {
        return lat >= -90.0 && lat <= 90.0
                && lon >= -180.0 && lon <= 180.0
                && !(lat == 0.0 && lon == 0.0);
    }

    private double distanceMeters(double startLat, double startLon, double endLat, double endLon) {
        float[] result = new float[1];
        Location.distanceBetween(startLat, startLon, endLat, endLon, result);
        return result[0];
    }

    @NonNull
    private String safe(@Nullable String value) {
        return value == null ? "" : value;
    }

    private void showLoading(boolean show) {
        if (pbLoading != null) {
            pbLoading.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void showError(@Nullable String message) {
        if (tvError == null) {
            return;
        }
        if (message == null || message.trim().isEmpty()) {
            tvError.setVisibility(View.GONE);
            tvError.setText("");
            return;
        }
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        if (webMap != null) {
            webMap.stopLoading();
            webMap.removeJavascriptInterface("AndroidMapBridge");
            webMap.setWebChromeClient(null);
            webMap.destroy();
            webMap = null;
        }
        pageReady = false;
        lastItemsPayload = "";
        pbLoading = null;
        tvError = null;
        tvNearestHighlight = null;
        super.onDestroyView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        boolean granted = false;
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_GRANTED) {
                granted = true;
                break;
            }
        }
        if (granted) {
            fetchCurrentLocation();
        }
    }

    private class MapJsBridge {
        @JavascriptInterface
        public void openDetail(String itemId) {
            mainHandler.post(() -> openItemDetail(itemId));
        }
    }
}
