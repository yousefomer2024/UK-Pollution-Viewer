## About the Project

PollutionViewer is a Java application that helps users explore air pollution data across major UK cities. It provides interactive heatmaps, trend graphs, and detailed statistics for pollutants like **NO2**, **PM2.5**, and **PM10**. Users can filter, compare, and search data by region, year, coordinates, or grid code. The app also integrates live air quality data from the **WAQI API**, making it a practical tool for environmental data exploration.

![Uploading image.png…]()


---

## Features

### Interactive Maps
- View pollution levels across **London, Manchester, Nottingham, and Birmingham**.  
- Heatmaps update based on **year** and **pollutant type**.  
- Hover over the map to see live **coordinates** and pollution readings.  
- Clickable markers provide nearby pollution details in popups.  
- Filter specific pollution levels for focused visualisation.

### Statistics & Trends
- Calculate **average** and **highest pollution levels** for selected regions, years, and pollutants.  
- Compare data between **two regions or years** side by side.  
- Interactive graphs with clickable points for detailed insights.  
- Custom filters allow for flexible analysis without changing underlying data.

### Search & Detailed Data
- Search by **grid code** or **coordinates** to find specific pollution data.  
- Popups let users select the year and pollutant for precise queries.

### User Experience
- Switch between **dark** and **light mode**.  
- Live coordinate tracking for immediate feedback.  
- Smooth navigation across map and graph views.

### Live Data Integration
- Fetch live air pollution data from the **WAQI API**.  
- Export DEFRA-compliant CSV files with pollutant information.  
- Background tasks ensure the interface remains responsive during data fetches.

---

## GUI Overview

- **MenuBar:** Quick access to Visualise, Location, Visuals, Search, and Help.  
- **TabPanes:** Separate Map and Graph views, plus Statistics and Filter tools.  
- **ScrollPane & Canvas:** Navigate and interact with maps efficiently.  
- **BorderPane Layout:** Central visualisation area with side panels for controls.  
- **Live Coordinate Label:** Shows current mouse position on the map.

---

## Implementation Highlights

### Core Features
1. **Welcome Panel:** Introduces users to the app with step-by-step instructions.  
2. **Data Visualisation Panel:** Heatmaps with scaled markers and blur effects for smooth visuals.  
3. **Pollution Statistics Panel:** Calculates average and peak pollution, including grid coordinates.  
4. **Detailed Grid Search:** Users can search by coordinates or grid codes.

### Advanced Features
1. **Custom Filters:** Adjust which pollution levels are displayed on the map.  
2. **Graph Trends:** Clickable, auto-updating graphs for average and peak pollution.  
3. **Comparison Tool:** Compare pollutant data between regions or years side by side.  
4. **Additional Map Regions:** Added Manchester, Nottingham, Birmingham.  
5. **Dynamic Statistics:** Stats update automatically when changing regions.  
6. **API Integration:** Live pollution data fetching and CSV export.

---

## Unit Testing

We tested the **Statistics class** to ensure accurate data analysis:  
- Correct average and highest pollution calculation  
- Boundary checks to filter points outside the selected region  
- Correct mapping of coordinates to regions  
- Handling empty datasets gracefully  

Unit tests ensure reliable calculations and robust data filtering.

---

## Known Issues

- Heatmap blur can slightly distort exact pollution values.  
- Some markers may overlap and become unclickable.  
- Slow WAQI API responses may delay CSV generation.  
- Occasional URL-to-URI conversion errors (non-critical).  
- Differences between API data and existing CSVs may cause slight visual discrepancies.

---

## Technologies & Libraries

- **JavaFX** – GUI and visualization  
- **Java** – Core application logic  
- **Gson** – JSON parsing  
- **jcoord** – Coordinate conversions  
- **WAQI API** – Live air quality data  
- **DEFRA-compliant CSV** – Data export

---

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/yousefomer2024/UK-Pollution-Viewer
