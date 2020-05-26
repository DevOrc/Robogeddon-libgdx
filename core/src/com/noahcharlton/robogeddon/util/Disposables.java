package com.noahcharlton.robogeddon.util;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Disposables {

    private final Array<Disposable> disposables = new Array<>();

    public void add(Disposable disposable){
        disposables.add(disposable);
    }

    public void dispose(){
        disposables.forEach(Disposable::dispose);
    }
}
