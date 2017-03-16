package com.sean.android.example.base.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Seonil on 2017-03-10.
 */

public class AppModelContainer<MODEL extends AppModel> extends AppModel implements Iterable<MODEL> {
    private List<MODEL> models;

    public AppModelContainer() {
        this.models = new ArrayList<>();
    }

    public void clear() {
        models.clear();
    }

    public boolean isEmpty() {
        return models.isEmpty();
    }

    public int count() {
        return models.size();
    }

    public MODEL get(int index) {
        return models.get(index);
    }

    public MODEL getFirst() {
        MODEL model = null;
        if (!isEmpty()) {
            model = models.get(0);
        }
        return model;
    }

    public MODEL getLast() {
        MODEL model = null;
        if (!isEmpty()) {
            model = models.get(count() - 1);
        }
        return model;
    }

    public void add(MODEL model) {
        models.add(model);
    }

    public void addAll(List<MODEL> models) {
        if (models != null) {
            this.models.addAll(models);
        }
    }

    public MODEL remove(int index) {
        return models.remove(index);
    }

    public void remove(MODEL model) {
        models.remove(model);
    }

    public List<MODEL> getModels() {
        return Collections.unmodifiableList(models);
    }

    @Override
    public Iterator<MODEL> iterator() {
        return null;
    }

    @Override
    public String toString() {
        return "AppModelContainer{" +
                "models=" + models.toString() +
                '}';
    }
}
