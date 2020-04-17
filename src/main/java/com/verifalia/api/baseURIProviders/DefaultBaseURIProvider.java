package com.verifalia.api.baseURIProviders;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class DefaultBaseURIProvider extends BaseURIProvider {
    private ArrayList<URI> baseURIs;

    public DefaultBaseURIProvider() {
        try {
            baseURIs = new ArrayList<URI>() {{
                add(new URI("https://api-1.verifalia.com"));
                add(new URI("https://api-2.verifalia.com"));
                add(new URI("https://api-3.verifalia.com"));
            }};
        } catch (URISyntaxException e) {
        }
    }

    public List<URI> provideBaseURIs() {
        return baseURIs;
    }
}
