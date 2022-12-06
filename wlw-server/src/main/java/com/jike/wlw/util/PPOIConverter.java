package com.jike.wlw.util;

import com.geeker123.rumba.commons.util.converter.Converter;
import com.geeker123.rumba.gaode.api.poi.POI;

public class PPOIConverter implements Converter<POI, PPOI> {

    private static PPOIConverter instance;

    public static PPOIConverter getInstance() {
        if (instance == null) {
            instance = new PPOIConverter();
        }
        return instance;
    }

    @Override
    public PPOI convert(POI source) throws Exception {
        if (source == null) {
            return null;
        }

        PPOI target = new PPOI();
        target.setAddress(source.getAddress());
        target.setPoiAddress(source.getPoiAddress());
        target.setPoiName(source.getPoiName());

        if (source.getLngLat() != null) {
            target.setLat(source.getLngLat().getLat());
            target.setLng(source.getLngLat().getLng());
        }
        if (source.getPcdt() != null) {
            target.setProvince(source.getPcdt().getProvince());
            target.setProvinceCode(source.getPcdt().getProvinceCode());
            target.setCity(source.getPcdt().getCity());
            target.setCityCode(source.getPcdt().getCityCode());
            target.setDistrict(source.getPcdt().getDistrict());
            target.setDistrictCode(source.getPcdt().getDistrictCode());
            target.setTownship(source.getPcdt().getTownship());
        }
        return target;
    }
}