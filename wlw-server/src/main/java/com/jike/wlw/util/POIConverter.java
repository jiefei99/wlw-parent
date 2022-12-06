package com.jike.wlw.util;

import com.geeker123.rumba.commons.util.converter.Converter;
import com.geeker123.rumba.gaode.api.lnglat.LngLat;
import com.geeker123.rumba.gaode.api.pcdt.PCDT;
import com.geeker123.rumba.gaode.api.poi.POI;
import org.springframework.beans.BeanUtils;

public class POIConverter implements Converter<PPOI, POI> {

    private static POIConverter instance;

    public static POIConverter getInstance() {
        if (instance == null) {
            instance = new POIConverter();
        }
        return instance;
    }

    @Override
    public POI convert(com.jike.wlw.util.PPOI source) throws Exception {
        if (source == null) {
            return null;
        }
        POI target = new POI();
        BeanUtils.copyProperties(source, target);

        LngLat lngLat = new LngLat();
        BeanUtils.copyProperties(source, lngLat);
        target.setLngLat(lngLat);

        PCDT pcdt = new PCDT();
        BeanUtils.copyProperties(source, pcdt);
        target.setPcdt(pcdt);

        return target;
    }
}