package com.collectpop.service;

import com.collectpop.domain.*;
import com.collectpop.dto.DecList;
import com.collectpop.dto.Pager;

import java.util.List;

public interface AdminService {
    void insertEx(String text);

    List<DecExample> getAllDecEx();

    void deleteExample(Long dleid);

    void addDeclaration(Declaration declaration);

    List<DecList> getdecListWithPaging(Pager pager);

    Long getTotaldec(Pager pager);

    //유저 스토어매니저로 승격
    void approveRequest(Users users);
    //유저 스토어매니저 거부
    void rejectRequest(StoreReject reject);

    List<StoreReject>  getStoreRejectByRequestId(Long requestId);

    void updateRequestStatus(StoreRequestVO requestById);
}
