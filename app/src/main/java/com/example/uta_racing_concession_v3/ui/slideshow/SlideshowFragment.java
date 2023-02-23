package com.example.uta_racing_concession_v3.ui.slideshow;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProvider;

import com.example.uta_racing_concession_v3.AccountModal;
import com.example.uta_racing_concession_v3.DBHandler;
import com.example.uta_racing_concession_v3.R;
import com.example.uta_racing_concession_v3.databinding.FragmentSlideshowBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private TableLayout table;
    private DBHandler dbHandler;
    private TableRow tr;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SlideshowViewModel.class);

        View v = inflater.inflate(R.layout.fragment_slideshow,container,false);
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        dbHandler = new DBHandler(getContext());

        ArrayList<AccountModal> accounts =(ArrayList<AccountModal>) dbHandler.getAccount(null);
        table = v.findViewById(R.id.account_table_display);

        /**
         * Begin Headers
         */
        tr = new TableRow(root.getContext());
        tr.setBackgroundColor(Color.GRAY);
        tr.setLayoutParams( new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));

        TextView label_ID = getTextView(10,"ID", Color.WHITE,Color.GRAY);
        tr.addView(label_ID);// add the column to the table row

        TextView label_name = getTextView(10,"NAME", Color.WHITE,Color.GRAY);
        tr.addView(label_name);// add the column to the table row

        TextView label_balance = getTextView(10,"BALANCE", Color.WHITE,Color.GRAY);
        tr.addView(label_balance);// add the column to the table row


        /**
         * Start on Table
         */
        table.addView(tr,new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));

        int len = accounts.size();
        for (int i = 0; i < len; i++){
            TableRow tr_1 = new TableRow(root.getContext());
            AccountModal account = accounts.get(i);
            int alternate = Color.GRAY;
            if(i%2==0){
                alternate = Color.BLACK;
            }
            //ID Row
            tr_1.addView(getTextView(i+1,account.getId().toString(),Color.WHITE,alternate));
            //Name Row
            tr_1.addView(getTextView(i+2000,account.getName(),Color.WHITE,alternate));
            //Balance Row
            tr_1.addView(getTextView(i+1000000,account.getBalance().toString(),Color.WHITE,alternate));
            table.addView(tr_1,new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
        }


        return v;
    }

    private TextView getTextView(int id, String title, int color, int bgColor) {
        TextView tv = new TextView(root.getContext());
        tv.setId(id);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
        tv.setPadding(20, 20, 20, 20);
        tv.setBackgroundColor(bgColor);
        return tv;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dbHandler.close();
        binding = null;
    }
}