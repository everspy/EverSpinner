package com.everspysolutions.everspinner.SynonymFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Iterator;

public class SynonymCacher {

    Hashtable<String, Synonym[]> cache;

    public SynonymCacher () {
        cache = new Hashtable<>();
    }

    public SynonymCacher (JSONObject object) {
        cache = new Hashtable<>();
        addFromJSONObject(object);
    }

    public void addItemToCache(String word, Synonym[] synonyms) {
        cache.put(word, synonyms);
    }

    public Synonym[] fetchSynonymsFromCache(String word) {
        if(cache.contains(word)) {
            return cache.get(word);
        }
        return null;
    }

    public Boolean cacheContains(String word) {
        return cache.contains(word);
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();

        try {
            for (String word : cache.keySet()) {
                JSONArray synonyms = new JSONArray();

                for(Synonym synonym : cache.get(word)) {
                    JSONObject syn = new JSONObject();
                    syn.put("word", synonym.getWord());
                    syn.put("score", synonym.getScore());
                    synonyms.put(syn);
                }

                jsonObject.put(word, synonyms);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public void addFromJSONObject(JSONObject object) {
        JSONParse parser = new JSONParse();
        Iterator<String> keys = object.keys();

        try {
            while (keys.hasNext()) {
                String key = keys.next();
                if (object.get(key) instanceof JSONArray) {
                    JSONArray obj = (JSONArray) object.get(key);
                    Synonym[] synonyms = parser.parseSynonyms(obj);

                    if(synonyms != null) {
                        cache.put(key, synonyms);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
