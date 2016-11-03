package com.kriska.customsearchview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kriska.customsearchview.R;
import com.kriska.customsearchview.adapter.SuggestionListRecyclerAdapter;
import com.kriska.customsearchview.interfaces.Suggestion;

import java.util.ArrayList;


public class CustomSearchView extends LinearLayout {

    EditText txtFieldSearch;
    RecyclerView rvSuggestionList;
    Boolean mListVisible;
    TextView txtCross;
    FontIcon fiBackIcon;

    Context ctx;
    ArrayList<Suggestion> receivedSuggestionList;
    SuggestionListRecyclerAdapter suggestionListAdapter;


    ClearClickedListener clearClickedListener = null;
    BackButtonClickListener backButtonClickListener = null;

    public CustomSearchView(Context context) {

        this(context, null);
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        Fonts.initializeFonts(context);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomSearch, 0, 0);

        int hintColor = a.getColor(R.styleable.CustomSearch_hintTextColor, ContextCompat.getColor(context, android.R.color.darker_gray));
        int textColor = a.getColor(R.styleable.CustomSearch_textColor, ContextCompat.getColor(context, android.R.color.black));
        String hintText = a.getString(R.styleable.CustomSearch_hintText);

//        String actionText=a.getString(R.styleable.CustomSearch_actionText);
        mListVisible = a.getBoolean(R.styleable.CustomSearch_showList, false);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_search_view, this, true);
        RelativeLayout mainLayout = (RelativeLayout) getChildAt(0);
        int backgroundColor = a.getColor(R.styleable.CustomSearch_backgroundColor, ContextCompat.getColor(context, android.R.color.darker_gray));
        a.recycle(); // a must not be reuesd after call of this function
        LinearLayout searchLayout = (LinearLayout) mainLayout.getChildAt(0);
        searchLayout.setBackgroundColor(backgroundColor);
        txtFieldSearch = (EditText) searchLayout.findViewById(R.id.txtFieldSearch);
        txtFieldSearch.setTextColor(textColor);
        txtFieldSearch.setHintTextColor(hintColor);

        setHintText(hintText);
        txtFieldSearch.addTextChangedListener(getTextWatcher(null));
        txtFieldSearch.setOnEditorActionListener(getEditorActionListener(null));

        txtCross = (TextView) searchLayout.findViewById(R.id.txtCross);
        txtCross.setOnClickListener(crossListener);
        fiBackIcon = (FontIcon) findViewById(R.id.fiBackIcon);
        fiBackIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backButtonClickListener != null) {
                    backButtonClickListener.backButtonCliked();
                }
            }
        });
        rvSuggestionList = (RecyclerView) mainLayout.getChildAt(1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ctx);
        rvSuggestionList.setLayoutManager(layoutManager);
        rvSuggestionList.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST, R.drawable.drawable_divider));


    }

    public void changeSearchIcon(Boolean enableBackButton, BackButtonClickListener backButtonClickListener) {
        if (enableBackButton) {
            this.backButtonClickListener = backButtonClickListener;
            fiBackIcon.setText("a");
        } else {
            fiBackIcon.setText("b");
        }
    }


    public void makeSuggestionListVisible(Boolean show) {
        mListVisible = show;
    }

    public void setHintText(String hint) {
        if (hint != null) {
            txtFieldSearch.setHint(hint);
        } else {
            txtFieldSearch.setHint("");
        }
    }

    public void setSuggetionListData(ArrayList<Suggestion> suggestionList, SuggestionListRecyclerAdapter.ListItemSelectListener listItemSelectListener) {
        if (suggestionList != null && mListVisible) {
            receivedSuggestionList = (ArrayList) suggestionList.clone();
            suggestionListAdapter = new SuggestionListRecyclerAdapter(ctx, suggestionList, listItemSelectListener);
            rvSuggestionList.setAdapter(suggestionListAdapter);
            rvSuggestionList.setVisibility(VISIBLE);
        } else if (suggestionList == null && mListVisible) {

            if (receivedSuggestionList.size() != 0) {
                rvSuggestionList.setVisibility(VISIBLE);
//                suggestionListAdapter.setList(receivedSuggestionList);
                rvSuggestionList.scrollToPosition(0);

            } else {
                rvSuggestionList.setVisibility(GONE);


            }

        } else {
            rvSuggestionList.setVisibility(GONE);


        }
    }

    public void hideList() {

        rvSuggestionList.setVisibility(GONE);
    }

    public void setTextWatcher(TextWatcher textWatcher) {
        txtFieldSearch.addTextChangedListener(getTextWatcher(textWatcher));
    }

    private TextWatcher getTextWatcher(final TextWatcher textWatcher) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (textWatcher != null) {
                    textWatcher.beforeTextChanged(s, start, count, after);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    txtCross.setVisibility(VISIBLE);
                } else {
                    txtCross.setVisibility(GONE);
                }
                if (mListVisible) {

                    suggestionListAdapter.setFilterInList(s.toString());
//                        suggestionListAdapter.setList(filteredModelList);
                    rvSuggestionList.scrollToPosition(0);
                    rvSuggestionList.setVisibility(VISIBLE);
                    setSuggetionListData(null, null);


                }

//                } else {
//                    if (mListVisible) {
//
//                        setSuggetionListData(null, null);
//                    }

//                }


                if (textWatcher != null) {
                    textWatcher.onTextChanged(s, start, before, count);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (textWatcher != null) {
                    textWatcher.afterTextChanged(s);
                }
            }


        };
    }


    public void setCustomOnEditorActionListener(TextView.OnEditorActionListener editorActionListener) {

        txtFieldSearch.setOnEditorActionListener(getEditorActionListener(editorActionListener));


    }

    private TextView.OnEditorActionListener getEditorActionListener(final TextView.OnEditorActionListener editorActionListener) {


        return new TextView.OnEditorActionListener() {


            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE)
                    suggestionListAdapter.setFilterInList(v.getText().toString());
                if (editorActionListener != null) {
                    editorActionListener.onEditorAction(v, actionId, event);
                }
                return true;
            }
        };
    }


    public void setCrossListener(ClearClickedListener clearlistener) {
        this.clearClickedListener = clearlistener;
    }


    private OnClickListener crossListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            clearText();
            setSuggetionListData(null, null);
            if (clearClickedListener != null) {
                clearClickedListener.clearClicked();
            }
        }
    };

    private void clearText() {
        txtFieldSearch.setText("");
    }


    public String getText() {
        return txtFieldSearch.getText().toString();
    }

    public interface ClearClickedListener {
        public void clearClicked();
    }

    public interface BackButtonClickListener {
        public void backButtonCliked();
    }


}
