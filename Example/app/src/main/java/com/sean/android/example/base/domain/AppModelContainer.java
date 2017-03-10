package com.sean.android.example.base.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Seonil on 2017-03-10.
 */

public class AppModelContainer<M extends AppModel> extends AppModel implements Iterable<M> {
    private List<M> models;

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

    public M get(int index) {
        return models.get(index);
    }

    public M getFirst() {
        M model = null;
        if (!isEmpty()) {
            model = models.get(0);
        }
        return model;
    }

    public M getLast() {
        M model = null;
        if (!isEmpty()) {
            model = models.get(count() - 1);
        }
        return model;
    }

    public void add(M model) {
        models.add(model);
    }

    public void addAll(List<M> models) {
        if (models != null) {
            this.models.addAll(models);
        }
    }

    public M remove(int index) {
        return models.remove(index);
    }

    public void remove(M model) {
        models.remove(model);
    }

    public List<M> getModels() {
        return Collections.unmodifiableList(models);
    }

    @Override
    public Iterator<M> iterator() {
        return null;
    }

    @Override
    public String toString() {
        return "AppModelContainer{" +
                "models=" + models.toString() +
                '}';
    }
}
