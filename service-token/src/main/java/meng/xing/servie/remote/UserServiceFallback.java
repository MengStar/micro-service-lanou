package meng.xing.servie.remote;

import meng.xing.servie.remote.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceFallback implements UserService {
    @Override
    public Date getLastPasswordResetByUsername(String username) {
        return null;
    }

    @Override
    public String getCorrectPasswordByUsername(String username) {
        return null;
    }
}
