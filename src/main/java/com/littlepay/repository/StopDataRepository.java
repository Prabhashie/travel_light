package com.littlepay.repository;

import com.littlepay.model.Stop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * This class is a static implementation of a StopData Repository.
 * It currently stores the maximum route leg cost from each stop in memory as a static HashMap.
 * In a real application, this class would typically interact with a database
 * to manage stop data depending on whether we need to cache some data to
 * expedite fast retrieval.
 *
 * Note: In this simplified implementation, we ignore the fact that the same stop
 * can be included in multiple routes with different charges.
 *
 * @author Sachi
 */
@Repository
public class StopDataRepository {
    private static final Logger LOG = LoggerFactory.getLogger(StopDataRepository.class);
    private static final Map<Integer, Double> MAX_DESTINATION_COST_FROM_STOP = Map.of(
            1, 7.30,
            2, 5.50,
            3, 7.30
    );

    /**
     * Retrieves the maximum route leg cost from a given stop.
     * The method uses the stop identifier to look up the cost in the static map.
     *
     * @param stop the stop for which to retrieve the maximum route leg cost
     * @return the maximum route leg cost from the stop, or null if no cost is found
     */
    public Double getMaxRouteLegCostFromStop(Stop stop) {
        LOG.trace("Getting max route leg cost from stop {}", stop.getStopId());
        return MAX_DESTINATION_COST_FROM_STOP.get(stop.getNumericId());
    }
}
