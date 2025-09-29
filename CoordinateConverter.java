import uk.me.jstott.jcoord.*;

/**
 * A utility class for converting Easting/Northing coordinates
 * (OSGB36) to Latitude/Longitude (WGS84) using JCoord.
 * 
 * @author Ozgur Dorunay
 * @version 24/03/25
 */
public class CoordinateConverter {

    /**
     * Converts Easting/Northing (OSGB36) to Latitude/Longitude (WGS84).
     *
     * @param easting The easting coordinate.
     * @param northing The northing coordinate.
     * @return A double array where [0] = latitude, [1] = longitude.
     */
    public static double[] convertToLatLon(int easting, int northing) {
        // Convert British National Grid coordinates to Lat/Lon
        OSRef osRef = new OSRef(easting, northing);
        LatLng latLng = osRef.toLatLng(); // Convert to WGS84

        return new double[]{latLng.getLat(), latLng.getLng()}; // {latitude, longitude}
    }

    /**
     * Example usage for testing purposes.
     */
    public static void main(String[] args) {
        int exampleEasting = 460500;
        int exampleNorthing = 1219500;

        double[] latLon = convertToLatLon(exampleEasting, exampleNorthing);
        System.out.printf("Latitude: %.6f, Longitude: %.6f%n", latLon[0], latLon[1]);
    }
}
