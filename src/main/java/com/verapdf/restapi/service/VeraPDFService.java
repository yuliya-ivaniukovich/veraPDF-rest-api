package com.verapdf.restapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.profiles.Profiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VeraPDFService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VeraPDFService.class);

    private List<String> featureList;
    private List<String> profilesList;

    public VeraPDFService() {
        featureList = prepareFeatureList();
        profilesList = prepareProfilesList();
    }

    private List<String> prepareFeatureList() {
        List<String> featureList = new ArrayList<>();
        for (FeatureObjectType type : FeatureObjectType.values()) {
            if (type != FeatureObjectType.ERROR) {
                featureList.add(type.getFullName());
            }
        }
        featureList.sort(String.CASE_INSENSITIVE_ORDER);
        return featureList;
    }

    private List<String> prepareProfilesList() {
        return Profiles.getVeraProfileDirectory().getPDFAFlavours().stream()
                .map(PDFAFlavour::getId)
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> getFeatureList() {
        return Collections.unmodifiableList(this.featureList);
    }

    public List<String> getProfilesList() {
        return Collections.unmodifiableList(this.profilesList);
    }
}
