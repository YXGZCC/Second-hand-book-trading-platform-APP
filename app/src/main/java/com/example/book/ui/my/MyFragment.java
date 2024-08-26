package com.example.book.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.book.Login;
import com.example.book.Logout;
import com.example.book.R;
import com.example.book.databinding.FragmentMyBinding;

public class MyFragment extends Fragment {

    private FragmentMyBinding binding;
    private TextView userNameTextView, accountTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyViewModel myViewModel =
                new ViewModelProvider(this).get(MyViewModel.class);

        binding = FragmentMyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 获取传递过来的用户信息
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            String userName = bundle.getString("user_name");
            int userId = bundle.getInt("user_id");

            userNameTextView = root.findViewById(R.id.text_user_name);
            accountTextView = root.findViewById(R.id.text_account);

            userNameTextView.setText("用户名：" + userName);
            accountTextView.setText("账号id：" + userId);
        }

        // 找到前往登录按钮
        Button loginButton = root.findViewById(R.id.button_login);

        // 设置按钮点击事件
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        });

        // 设置注销按钮
        Button logoutButton = binding.buttonLogout;
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用 Logout 类进行用户注销
                Logout logout = new Logout(getContext());
                logout.logoutUser(getContext());
                getActivity().finish();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
