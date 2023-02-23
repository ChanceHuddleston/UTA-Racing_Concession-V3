package com.example.uta_racing_concession_v3.ui.home;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProvider;
import android.widget.Toast;

import com.example.uta_racing_concession_v3.AccountModal;
import com.example.uta_racing_concession_v3.DBHandler;
import com.example.uta_racing_concession_v3.R;
import com.example.uta_racing_concession_v3.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private EditText account_num_input;
    private Button cent_25_button,cent_50_button,cent_75_button,cent_100_button;
    private  FloatingActionButton charge_account_button, add_funds_button;
    private TextView total_value_text,account_name,account_balance;
    private DBHandler dbHandler;
    boolean accountOpen = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        View v = inflater.inflate(R.layout.fragment_home,container,false);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbHandler = new DBHandler(getContext());

        //Setup Interfacing entities
        cent_25_button = v.findViewById(R.id.cent_25_button);
        cent_50_button = v.findViewById(R.id.cent_50_button);
        cent_75_button = v.findViewById(R.id.cent_75_button);
        cent_100_button = v.findViewById(R.id.cent_100_button);

        charge_account_button = v.findViewById(R.id.purchase_button);
        add_funds_button = v.findViewById(R.id.add_funds_button);

        total_value_text = v.findViewById(R.id.total_value_text);
        account_name = v.findViewById(R.id.name_display_text);
        account_balance= v.findViewById(R.id.account_balance_display_text);

        //Setting up account information gathering (Last 4 of ID)
        account_num_input = v.findViewById(R.id.account_num_user_input);
        //Account information action listener, when Done pressed
        account_num_input.setOnEditorActionListener((v1, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                String account_num_str = account_num_input.getText().toString();
                if(account_num_str.isEmpty()){
                    Toast.makeText(root.getContext(), "Enter all data", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else{
                    Integer account_num = Integer.parseInt(account_num_str);
                    AccountModal accountModal = (AccountModal) dbHandler.getAccount(account_num);
                    if(accountModal == null){
                        Toast.makeText(root.getContext(), "Account not found", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    Double balance = accountModal.getBalance();
                    String name = accountModal.getName();
                    account_balance.setText(balance.toString());
                    account_name.setText(name);
                    accountOpen=true;

                }
            }
            return false;
        });

        /**
         * Update database with the charged funds button
         */
    charge_account_button.setOnClickListener(view -> {
        Double chargeValue = Double.parseDouble((String) total_value_text.getText());
        Double balance;

        if(accountOpen){
            String account_num_str = account_num_input.getText().toString();
            Integer account_num = Integer.parseInt(account_num_str);
            AccountModal accountModal = (AccountModal) dbHandler.getAccount(account_num);
            balance = accountModal.getBalance();
            balance = balance - chargeValue;

            accountModal.setBalance(balance);
            account_balance.setText("");
            account_name.setText("");
            account_num_input.setText("");
            total_value_text.setText("0.00");

            dbHandler.updateAccount(accountModal);
            accountOpen = false;
        }
        else{
            Toast.makeText(root.getContext(), "Enter your ID first", Toast.LENGTH_SHORT).show();
            return;
        }
    });

        /**
         * Update database with the add funds button
         */
    add_funds_button.setOnClickListener(view->{
        Double chargeValue = Double.parseDouble((String) total_value_text.getText());
        Double balance;

        if(accountOpen){
            String account_num_str = account_num_input.getText().toString();
            Integer account_num = Integer.parseInt(account_num_str);
            AccountModal accountModal = (AccountModal) dbHandler.getAccount(account_num);
            balance = accountModal.getBalance();
            balance = balance + chargeValue;

            accountModal.setBalance(balance);
            account_balance.setText("");
            account_name.setText("");
            account_num_input.setText("");
            total_value_text.setText("0.00");

            dbHandler.updateAccount(accountModal);
            accountOpen = false;
        }
        else{
            Toast.makeText(root.getContext(), "Enter your ID first", Toast.LENGTH_SHORT).show();
            return;
        }
    });

        return v;
    }


    @Override
    public void onDestroyView() {
        dbHandler.close();
        super.onDestroyView();
        binding = null;
    }
}