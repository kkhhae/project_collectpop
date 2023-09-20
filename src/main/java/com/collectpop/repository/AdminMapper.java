package com.collectpop.repository;

import com.collectpop.domain.DecExample;
import com.collectpop.domain.Declaration;
import com.collectpop.domain.StoreReject;
import com.collectpop.domain.StoreRequestVO;
import com.collectpop.dto.Pager;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    void insertEx(DecExample decExample);

    List<DecExample> getAllDecEx();

    void deleteExample(Long dleid);

    void addDeclaration(Declaration declaration);

    int getDeclaration(Declaration declaration);

    void updateDeclaration(Declaration declaration);

    List<Declaration> getdecListWithPaging(Pager pager);

    Long getTotaldec(Pager pager);

    String getdeclContent(Long did);

    void rejectRequest(StoreReject reject);

    List<StoreReject>  getStoreRejectByRequestId(Long requestId);

    void updateRequestStatus(StoreRequestVO requestById);
}
