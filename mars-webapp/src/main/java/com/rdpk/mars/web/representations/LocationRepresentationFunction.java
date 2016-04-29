package com.rdpk.mars.web.representations;

import com.rdpk.mars.Location;

import java.util.function.Function;

public class LocationRepresentationFunction implements Function<Location, LocationRepresentation> {
    @Override
    public LocationRepresentation apply(Location location) {
        return new LocationRepresentation(location.getX(), location.getY());
    }
}
