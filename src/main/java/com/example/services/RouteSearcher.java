package com.example.services;


import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * Осуществояет поиск пары остановок
 */
@Service
public class RouteSearcher {

    private final RoutesLoader loader;
    /**
     * Кеш остановок для быстрого поиска
     * Данные хранятся в виде: id остановки -> [массив id маршрутов, содержащих эту остановку]
     */
    private final Map<Integer, List<Integer>> stationsCache = new HashMap<>();

    public RouteSearcher(RoutesLoader loader) {
        this.loader = loader;
        System.out.println("Creating stations cache...");
        fillCache();
        System.out.println("Stations cache created.");
    }

    /**
     * Наполняет кеш значениями из файла данных
     */
    private void fillCache() {
        try(BufferedReader reader = loader.getRoutesReader()) {
            String nextRoute = reader.readLine();
            while (nextRoute != null) {
                String[] ids = nextRoute.split(" ");
                Integer routeId = Integer.valueOf(ids[0]);
                for (int i = 1; i < ids.length; i++) {
                    Integer stationId = Integer.valueOf(ids[i]);
                    List<Integer> stationCache = stationsCache.computeIfAbsent(stationId, id -> new ArrayList<>(50));
                    stationCache.add(routeId);
                }
                nextRoute = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Выполняет поиск пары остановок
     *
     * @param from  id начальной остановки
     * @param to    id конечной остановки
     * @return      id маршрута, содержащего искомую пару остановок, либо null, если маршрут не найден
     */
    public Integer search(String from, String to) {
        if (from.equals(to)) {
            return null;
        }
        List<Integer> routesWithFrom = stationsCache.get(Integer.valueOf(from));
        List<Integer> routesWithTo = stationsCache.get(Integer.valueOf(to));
        if (routesWithFrom == null || routesWithTo == null) {
            return null;
        }
        Set<Integer> routesWithFromTo = new HashSet<>();
        if (routesWithFrom.size() <= routesWithTo.size()) {
            for(Integer routeId : routesWithFrom) {
                if (routesWithTo.contains(routeId)) {
                    routesWithFromTo.add(routeId);
                }
            }
        } else {
            for(Integer routeId : routesWithTo) {
                if (routesWithFrom.contains(routeId)) {
                    routesWithFromTo.add(routeId);
                }
            }
        }
        if (routesWithFromTo.isEmpty()) {
            return null;
        }
        for (Integer routeId : routesWithFromTo) {
            if(searchInRoute(routeId, from, to)) {
                return routeId;
            }
        }
        return null;
    }

    private boolean searchInRoute(Integer routeId, String from, String to) {
        String route = loader.getRoute(String.valueOf(routeId));
        return route.indexOf(from) < route.indexOf(to);
    }

    public Map<Integer, List<Integer>> getStationsCache() {
        return stationsCache;
    }

}
