# Vietnam Map Demo (Leaflet + OpenStreetMap + Overpass)

This demo is a standalone HTML page that:
- Centers the map at Vietnam (`lat: 16.0`, `lon: 108.0`, zoom `6`)
- Uses OpenStreetMap tiles via Leaflet
- Fetches tourism points from Overpass API (`attraction`, `museum`, `hotel`)
- Renders markers with popup info (`name`, `description`, optional image)
- Handles API errors gracefully

## File
- `vietnam-map-osm.html`

## One-click run (recommended)
If your IDE shows a Run button for npm scripts, use script `start`.

```powershell
Set-Location "C:\2025-2026HocKy2\LapTrinhDiDong\LTDD"
npm start
```

This opens:
- `http://127.0.0.1:8080/vietnam-map-osm.html`

## Manual run options
Open the file directly in a browser, or run a local static server:

```powershell
Set-Location "C:\2025-2026HocKy2\LapTrinhDiDong\LTDD"
python -m http.server 8080
```

Then open:
- `http://127.0.0.1:8080/vietnam-map-osm.html`

## Notes
- Data quantity depends on current OpenStreetMap/Overpass availability.
- Overpass can be slow or rate-limited; the page shows a user-friendly error message when that happens.


