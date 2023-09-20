package com.collectpop.service;

import com.collectpop.domain.*;
import com.collectpop.dto.Pager;
import com.collectpop.dto.DecList;
import com.collectpop.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService{
    private final AdminRepository adminRepository;
    private final UsersService usersService;

    @Override
    public void insertEx(String text) {
        DecExample decExample = new DecExample();
        decExample.setDleContent(text);
        adminRepository.insertEx(decExample);
    }

    @Override
    public List<DecExample> getAllDecEx() {
        return adminRepository.getAllDecEx();
    }

    @Override
    public void deleteExample(Long dleid) {
        adminRepository.deleteExample(dleid);
    }

    @Override
    public void addDeclaration(Declaration declaration) {
        int num = adminRepository.getDeclaration(declaration);
        if(num == 0) {
            adminRepository.addDeclaration(declaration);
        }else {
            adminRepository.updateDeclaration(declaration);
        }
    }

    @Override
    public List<DecList> getdecListWithPaging(Pager pager) {
        List<Declaration> declarations = adminRepository.getdecListWithPaging(pager);
        DecList decList = new DecList();
        List<DecList> decLists = new ArrayList<>();
        for (Declaration declaration : declarations) {
            decList.setDid(declaration.getDid());
            decList.setFid(declaration.getFid());
            decList.setDecnum(declaration.getDecnum());
            decList.setUserId(declaration.getUserId());
            decList.setDeclContent(adminRepository.getdeclContent(declaration.getDleid()));
            decLists.add(decList);
        }
        return decLists;
    }

    @Override
    public Long getTotaldec(Pager pager) {
        return adminRepository.getTotaldec(pager);
    }


    @Override
    public void approveRequest(Users users) {

        users.setRole_store(Role.STOREMANAGER);
        usersService.changeStore(users.getUserId());
        log.info("updateStore after User : {}", users);

    }

    @Override
    public void rejectRequest(StoreReject reject) {
        adminRepository.rejectRequest(reject);
    }

    @Override
    public List<StoreReject>  getStoreRejectByRequestId(Long requestId) {
        return adminRepository.getStoreRejectByRequestId(requestId);
    }

    @Override
    public void updateRequestStatus(StoreRequestVO requestById) {
        adminRepository.updateRequestStatus(requestById);
    }
}
