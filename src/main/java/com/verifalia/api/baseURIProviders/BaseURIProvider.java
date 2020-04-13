package com.verifalia.api.baseURIProviders;

import java.net.URI;
import java.util.ArrayList;

public abstract class BaseURIProvider {
    public abstract ArrayList<URI> provideBaseURIs();
}
