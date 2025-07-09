package com.littlepay.repository;

import java.util.Map;

import com.littlepay.model.Stop;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * This class is a static implementation of a RouteData Repository.
 * It currently stores the route leg costs in memory as a static HashMap.
 * In a real application, this class would typically interact with a database
 * to manage route leg costs depending on whether we need to cache some data to
 * expedite fast retrieval.
 *
 * Note: In this simplified implementation, we ignore the fact that the same stop
 * can be included in multiple routes with different charges.
 *
 * @author Sachi
 */
@Repository
public class RouteDataRepository {
    private static final Logger LOG = LoggerFactory.getLogger(RouteDataRepository.class);
    private static final Map<Pair<Integer, Integer>, Double> ROUTE_LEG_COST_MAP = Map.of(
                    Pair.of(1, 2), 3.25,
                    Pair.of(2, 3), 5.50,
                    Pair.of(1, 3), 7.30
    );

    /**
     * Retrieves the charge between two stops.
     * The method normalizes the stop identifiers to ensure consistent keying in the map.
     *
     * @param startStop the starting stop
     * @param finishStop the finishing stop
     * @return the charge between the two stops, or null if no charge is found
     */
    public Double getChargeBetween(Stop startStop, Stop finishStop) {
        LOG.trace("Getting charge between {} and {}", startStop, finishStop);
        return ROUTE_LEG_COST_MAP.get(normalize(startStop.getNumericId(), finishStop.getNumericId()));
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