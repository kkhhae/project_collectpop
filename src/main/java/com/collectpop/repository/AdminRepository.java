package com.collectpop.repository;

import com.collectpop.domain.DecExample;
import com.collectpop.domain.Declaration;
import com.collectpop.domain.StoreReject;
import com.collectpop.domain.StoreRequestVO;
import com.collectpop.dto.Pager;

import java.util.List;

public interface AdminRepository {
    void insertEx(DecExample decExample);

    List<DecExample> getAllDecEx();

    void deleteExample(Long dleid);

    void addDeclaration(Declaration declaration);

    int getDeclaration(Declaration declaration);

    void updateDeclaration(Declaration declaration);

    List<Declaration> getdecListWithPaging(Pager pager);

    Long getTotaldec(Pager pager);

    String getdeclContent(Long did);

    //스토어매니저 승인 거부
    void rejectRequest(StoreReject reject);

    List<StoreReject>  getStoreRejectByRequestId(Long requestId);

    void updateRequestStatus(StoreRequestVO requestById);
}
