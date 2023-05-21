package groupquattro.demo.services;

import groupquattro.demo.model.Wallet;
import groupquattro.demo.repos.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService {

    @Autowired
    private WalletRepository wr;

    public List<Wallet> allWallets(){
        return wr.findAll();
    }
}
