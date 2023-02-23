package com.example.uta_racing_concession_v3.ui.gallery;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProvider;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uta_racing_concession_v3.DBHandler;
import com.example.uta_racing_concession_v3.MainActivity;
import com.example.uta_racing_concession_v3.R;
import com.example.uta_racing_concession_v3.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private EditText newIDNumInput, newNameInput, startingBalanceInput;

    private FloatingActionButton addAccountBtn,rmvAccountBtn;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(GalleryViewModel.class);

        View v = inflater.inflate(R.layout.fragment_gallery,container,false);
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DBHandler dbHandler = new DBHandler(getContext());

        //Initialize ADD/REMOVE ACCOUNT variables
        newIDNumInput = v.findViewById(R.id.new_id_num_input);
        newNameInput = v.findViewById(R.id.new_name_input);
        startingBalanceInput = v.findViewById(R.id.new_starting_balance_input);
        addAccountBtn = v.findViewById(R.id.add_account_button);
        rmvAccountBtn = v.findViewById(R.id.remove_account_button);

        //Add on click listener for Add Account button
        addAccountBtn.setOnClickListener(view -> {
            String id_str = newIDNumInput.getText().toString();
            String name = newNameInput.getText().toString();
            String bal_str = startingBalanceInput.getText().toString();

            if(id_str.isEmpty() || name.isEmpty() || bal_str.isEmpty() || bal_str==null || name ==null){
                Toast.makeText(root.getContext(), "Enter all data", Toast.LENGTH_SHORT).show();
                return;
            }
            Integer id = Integer.parseInt(id_str);
            Double startBal = Double.parseDouble(bal_str);
            dbHandler.addAccount(name,id,startBal);

            Toast.makeText(root.getContext(), "Account Added", Toast.LENGTH_SHORT).show();
            newIDNumInput.setText("");
            newNameInput.setText("");
            startingBalanceInput.setText("");

        });

        rmvAccountBtn.setOnClickListener(view -> {
            String id_str = newIDNumInput.getText().toString();
            if(id_str.isEmpty()){
                Toast.makeText(root.getContext(), "Enter the ID", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!dbHandler.delAccount(Integer.parseInt(id_str))){
                Toast.makeText(root.getContext(), "FAIL: Account Balance negative or does not exist", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(root.getContext(), "SUCCESS: Account Deleted", Toast.LENGTH_SHORT).show();
            }

        });

        return v;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}