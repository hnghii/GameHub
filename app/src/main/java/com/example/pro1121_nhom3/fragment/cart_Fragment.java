package com.example.pro1121_nhom3.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pro1121_nhom3.MainActivity;
import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.adapter.gameAdapter;
import com.example.pro1121_nhom3.adapter.gameCartAdapter;
import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.hoadon;
import com.example.pro1121_nhom3.model.nguoidung;
import com.example.pro1121_nhom3.pagegameActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class cart_Fragment extends Fragment {

    ArrayList<game> listgame;
    RecyclerView rcvcart;
    TextView tvtongtien, tvwallet, tvtenusercart;
    ImageView imgusercart;
    Button btbuy;

    public cart_Fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_, container, false);

        tvtongtien = view.findViewById(R.id.tvtongcongcart);
        tvwallet = view.findViewById(R.id.tvwalletcart);
        imgusercart = view.findViewById(R.id.imgusercart);
        tvtenusercart = view.findViewById(R.id.tvtenusercart);
        btbuy = view.findViewById(R.id.btbuycart);
        rcvcart = view.findViewById(R.id.rcvcart);
        listgame = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        gameCartAdapter cartAdapter = new gameCartAdapter(listgame, getActivity());
        cartAdapter.getCartList(listgame);
        rcvcart.setAdapter(cartAdapter);
        rcvcart.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcvcart.addItemDecoration(dividerItemDecoration);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
        {
            String userEmail = currentUser.getEmail();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");
            Query query = databaseReference.orderByChild("email").equalTo(userEmail);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                            //cap nhat thong tin nguoidung
                            nguoidung nguoidung1 = userSnapshot.getValue(nguoidung.class);
                            nguoidung1.setTendangnhap(userSnapshot.getKey());

                            tvtenusercart.setText(nguoidung1.getTennd());
                            Glide.with(getActivity()).load(nguoidung1.getAvatar()).into(imgusercart);
                            DatabaseReference walletref = userSnapshot.child("wallet").getRef();
                            walletref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    float current = snapshot.getValue(Float.class);

                                    // Định dạng số dư ví thành định dạng tiền tệ VND
                                    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                                    String walletInVND = format.format(current);

                                    tvwallet.setText(walletInVND);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle error
                                }
                            });

                            DatabaseReference ref = userSnapshot.child("cart").getRef();
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    float tong = 0;
                                    for(DataSnapshot gamesnap1 : snapshot.getChildren()) {
                                        game game1 = gamesnap1.getValue(game.class);
                                        tong += game1.getGiaban();
                                    }

                                    // Định dạng tổng tiền thành định dạng tiền tệ VND
                                    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                                    String totalInVND = format.format(tong);

                                    tvtongtien.setText(totalInVND);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle error
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi nếu có
                }
            });
        }

        btbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Thanh toán");
                builder.setMessage("Bạn đang thanh toán toàn bộ game có trong giỏ hàng. Đồng ý thanh toán?");
                builder.setIcon(R.drawable.wallet);
                builder.setNegativeButton("HUỶ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton("THANH TOÁN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String walletString = tvwallet.getText().toString().replaceAll("[^0-9]", "");
                        String totalString = tvtongtien.getText().toString().replaceAll("[^0-9]", "");
                        float uwallet = Float.parseFloat(walletString);
                        float utotal = Float.parseFloat(totalString);

                        if(utotal > uwallet)
                        {
                            Toast.makeText(getActivity(), "Wallet của bạn không đủ tiền! Hãy nạp tiền vào Wallet", Toast.LENGTH_SHORT).show();
                        }else{
                            float uleft = uwallet - utotal;

                            String userEmail = currentUser.getEmail();

                            DatabaseReference hoadonRef = FirebaseDatabase.getInstance().getReference("hoadon");


                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");
                            Query query = databaseReference.orderByChild("email").equalTo(userEmail);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                            DatabaseReference cartRef = userSnapshot.child("cart").getRef();
                                            nguoidung nguoidung1 = userSnapshot.getValue(nguoidung.class);
                                            userSnapshot.child("wallet").getRef().setValue(uleft);
                                            tvwallet.setText(nguoidung1.getWallet()+"");
                                            cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    DatabaseReference ref2 = userSnapshot.child("game").getRef();
                                                    for(DataSnapshot cartgamesnap1 : snapshot.getChildren())
                                                    {
                                                        game game1 = cartgamesnap1.getValue(game.class);
                                                        game1.setMagame(cartgamesnap1.getKey());
                                                        ref2.child(game1.getMagame()).setValue(game1);
                                                        Date c = Calendar.getInstance().getTime();
                                                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                                        String homnay = df.format(c);
                                                        game1.setMagame(cartgamesnap1.getKey());
                                                        nguoidung1.setTendangnhap(userSnapshot.getKey());
                                                        hoadon newhd = new hoadon(game1, nguoidung1, homnay);
                                                        hoadonRef.push().setValue(newhd);

                                                        final Handler handler = new Handler();
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                DatabaseReference gameRef = FirebaseDatabase.getInstance().getReference("game");
                                                                gameRef.child(game1.getMagame()).child("sellcount").setValue(ServerValue.increment(1));
                                                            }
                                                        }, 5000);


                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                            cartRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getActivity(), "Đã thanh toán thành công!", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Xử lý lỗi nếu có
                                }
                            });
                        }
                    }
                });

                builder.show();
            }
        });

        return view;
    }

}