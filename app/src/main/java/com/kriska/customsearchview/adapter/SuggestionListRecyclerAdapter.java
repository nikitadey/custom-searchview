package com.kriska.customsearchview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kriska.customsearchview.R;
import com.kriska.customsearchview.Utitity.Constants;
import com.kriska.customsearchview.interfaces.Suggestion;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by nikita on 11/3/15.
 */
public class SuggestionListRecyclerAdapter extends RecyclerView.Adapter<SuggestionListRecyclerAdapter.ViewHolder> {

    private LinkedList<Suggestion> suggestionList = new LinkedList<>();
    private Context context;
    private ListItemSelectListener listItemSelectListener;
    private List<Suggestion> tempList = new LinkedList<>();

    public SuggestionListRecyclerAdapter(final Context context, List<Suggestion> suggestionList, ListItemSelectListener listItemSelectListener) {
        this.suggestionList.addAll(suggestionList);
        tempList = suggestionList;
        addHeader();
        this.context = context;
        this.listItemSelectListener = listItemSelectListener;
    }

    public void addHeader() {
        Suggestion suggestionModel = new Suggestion() {
            @Override
            public String getTitle() {
                return context.getString(R.string.suggested_search_result);
            }

            @Override
            public String getSubTitle() {
                return "";
            }
        };
        if (!suggestionList.contains(suggestionModel)) {
            this.suggestionList.addFirst(suggestionModel);
        }
    }

    public void setList(List<Suggestion> list) {
        suggestionList.clear();
        if (suggestionList != null) {
            this.suggestionList.addAll(list);
            addHeader();
            notifyDataSetChanged();
        }
    }

    public void setFilterInList(String query) {
        if (query != null || !query.isEmpty()) {
            List<Suggestion> list = filterModels(query);
            setList(list);
        } else {
            setList(tempList);
        }
    }

    private List<Suggestion> filterModels(String query) {

        query = query.trim().toLowerCase();
        List<Suggestion> filteredModelList = new LinkedList<>();
        if (tempList != null) {
            for (Suggestion model : tempList) {
                String title = model.getTitle().toLowerCase();
                String subTitle = model.getSubTitle().toLowerCase();
                if (title.contains(query) || subTitle.contains(query)) {
                    filteredModelList.add(model);
                }

            }
        }

        return filteredModelList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return Constants.HEADER_TYPE;
        } else {
            return Constants.ITEM_TYPE;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == Constants.HEADER_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_list_layout, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView title = holder.title;
        TextView subTitle = holder.subTitle;
        TextView header = holder.txtName;

        if (position != 0) {
            try {
                if (!suggestionList.get(position).getTitle().isEmpty()) {
                    title.setText(suggestionList.get(position).getTitle());
                    title.setVisibility(View.VISIBLE);


                } else {
                    throw new NullPointerException();
                }

            } catch (Exception e) {
                title.setVisibility(View.GONE);
            }

            try {
                if (!suggestionList.get(position).getSubTitle().isEmpty()) {
                    subTitle.setText(suggestionList.get(position).getSubTitle());
                    subTitle.setVisibility(View.VISIBLE);

                } else {
                    throw new NullPointerException();
                }
            } catch (Exception e) {
                subTitle.setVisibility(View.GONE);
            }
            View view = title.getRootView();
            view.setTag(suggestionList.get(position));
            view.setOnClickListener(clickListener);
        } else {
            try {
                header.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.textSizeSmall));
                if (getItemCount() > 1) {
                    header.setText(suggestionList.get(position).getTitle());
                } else {
                    header.setText(context.getString(R.string.no_search_result));
                }
            } catch (Exception e) {

            }
        }


    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            notifyDataSetChanged();
            if (listItemSelectListener != null) {
                listItemSelectListener.itemSelected(v.getTag());
            }
        }
    };

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {

        return suggestionList.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, subTitle, txtName;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            subTitle = (TextView) itemView.findViewById(R.id.subTitle);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
        }


    }


    public interface ListItemSelectListener {
        public void itemSelected(Object object);
    }
}
