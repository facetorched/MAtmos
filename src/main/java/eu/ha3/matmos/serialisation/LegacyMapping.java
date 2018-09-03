package eu.ha3.matmos.serialisation;

import java.util.AbstractMap;

@SuppressWarnings("serial")
public class LegacyMapping extends AbstractMap.SimpleEntry<String, String> {

    public LegacyMapping(String sheet, String index) {
        super(sheet, index);
    }
}
