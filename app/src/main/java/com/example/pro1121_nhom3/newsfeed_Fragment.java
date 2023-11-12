package com.example.pro1121_nhom3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pro1121_nhom3.adapter.gameAdapter;
import com.example.pro1121_nhom3.dao.gameDAO;
import com.example.pro1121_nhom3.model.game;

import java.util.ArrayList;
import java.util.TooManyListenersException;


public class newsfeed_Fragment extends Fragment {



    public newsfeed_Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    RecyclerView rcvAllgame;
    ArrayList<game> listGame;
    gameDAO GameDAO;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed_, container, false);

        GameDAO = new gameDAO(getActivity());
        rcvAllgame = view.findViewById(R.id.rcvAllGame);
        listGame = new ArrayList<>();

        InitUI();
        GameDAO.getAllGame(listGame);
        return view;
    }

    private void InitUI()
    {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvAllgame.setLayoutManager(linearLayoutManager);
        gameAdapter allgameAdapter = new gameAdapter(listGame, getActivity());
        rcvAllgame.setAdapter(allgameAdapter);
    }
}