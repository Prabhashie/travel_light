package com.littlepay.repository;

import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Repository;

/**
 * This class is a placeholder for the RouteCharges repository.
 * It currently stores the route charge data in memory as a static adjacency matrix.
 * In a real application, this class would typically interact with a database
 * to manage route charge data.
 *
 * @author Sachi
 */
@Repository
public class RouteChargesRepository {
    private static final Map<Pair<Integer, Integer>, Double> map = Map.of(
                    Pair.of(1, 2), 3.25,
                    Pair.of(2, 3), 5.50,
                    Pair.of(1, 3), 7.30
    );

    /**
     * Retrieves the charge for a given route defined by two stops.
     *
     * @param i the first stop identifier
     * @param j the second stop identifier
     * @return the charge for the route, or null if no charge is defined
     */
    public static Double get(int i, int j) {
        return map.get(normalize(i, j));
    }

    /**
     * Normalizes the route stops to ensure the first stop is always less than or equal to the second.
     * This is necessary for consistent keying in the map.
     *
     * @param i the first stop identifier
     * @param j the second stop identifier
     * @return a Pair representing the normalized stops
     */
    private static Pair<Integer, Integer> normalize(int i, int j) {
        return i <= j ? Pair.of(i, j) : Pair.of(j, i);
    }

}