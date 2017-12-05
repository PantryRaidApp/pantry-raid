package group12.tcss450.uw.edu.appproject.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import group12.tcss450.uw.edu.appproject.database.DBManager;
import group12.tcss450.uw.edu.appproject.R;

/**
 * An adapter class to measure String similarity based on the Levenshtein distance.
 */

public class SimilarityAdapter extends BaseAdapter implements Filterable {
    private static String[] ORIGINAL;
    private List<String> ingredients;
    private Context context;

    static {
        try {
            ORIGINAL = DBManager.getIngredients();
        } catch (Exception e) {
            ORIGINAL = new String[0];
            Log.d("TEST", "db error in adapter");
        }
    }

    public SimilarityAdapter(Context context) {
        this.context = context;
        ingredients = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public String getItem(int i) {
        return ingredients.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.autocomplete_item,
                    viewGroup,
                    false);
        }
        try {
            String name = getItem(index);
            TextView textView = (TextView) view.findViewById(R.id.ac_text);
            textView.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object result) {
                return ((String) result);
            }

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString().toLowerCase().trim();
                FilterResults results = new FilterResults();
                TreeMap<Integer, String> sortedResults = new TreeMap<Integer, String>();
                for (String s : ORIGINAL) {
                    int score = StringUtils.getLevenshteinDistance(query, s);
                    if (!s.contains(query)) {
                        score += 100;
                    }
                    String[] pieces = s.split(" ");
                    boolean found = false;
                    boolean foundBoth = false;
                    for (int i = 0; i < pieces.length; i++) {
                        if (pieces[i].startsWith(query)) {
                            found = true;
                        }
                        if (pieces[i].charAt(0) == query.charAt(0) && pieces[i].charAt(pieces[i].length() - 1) ==
                                query.charAt(query.length() - 1)) {
                            foundBoth = true;
                        }
                    }
                    if (!found)
                        score += 25;
                    if (!foundBoth)
                        score += 15;
                    sortedResults.put(score, s);
                }
                Iterator iterator = sortedResults.entrySet().iterator();
                List<String> finalResults = new ArrayList<String>();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    finalResults.add((String) entry.getValue());
                    Log.d("SAdapter", entry.getValue()+": "+entry.getKey());
                }
                results.values = finalResults;
                results.count = finalResults.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ingredients.clear();
                if (filterResults != null && filterResults.count > 0) {
                    for (Object o : (List<?>) filterResults.values) {
                        ingredients.add((String) o);
                    }
                    notifyDataSetChanged();
                } else if (charSequence == null) {
                    for (String s : ORIGINAL) {
                        ingredients.add(s);
                    }
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
