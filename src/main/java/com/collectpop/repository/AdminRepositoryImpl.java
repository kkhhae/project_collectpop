package com.collectpop.repository;

import com.collectpop.domain.DecExample;
import com.collectpop.domain.Declaration;
import com.collectpop.domain.StoreReject;
import com.collectpop.domain.StoreRequestVO;
import com.collectpop.dto.Pager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminRepositoryImpl implements AdminRepository{
    private final AdminMapper adminMapper;
    @Override
    public void insertEx(DecExample decExample) {
        adminMapper.insertEx(decExample);
    }

    @Override
    public List<DecExample> getAllDecEx() {
        return adminMapper.getAllDecEx();
    }

    @Override
    public void deleteExample(Long dleid) {
        adminMapper.deleteExample(dleid);
    }

    @Override
    public void addDeclaration(Declaration declaration) {
        adminMapper.addDeclaration(declaration);
    }

    @Override
    public int getDeclaration(Declaration declaration) {
        return adminMapper.getDeclaration(declaration);
    }

    @Override
    public void updateDeclaration(Declaration declaration) {
        adminMapper.updateDeclaration(declaration);
    }

    @Override
    public List<Declaration> getdecListWithPaging(Pager pager) {
        return adminMapper.getdecListWithPaging(pager);
    }

    @Override
    public Long getTotaldec(Pager pager) {
        return adminMapper.getTotaldec(pager);
    }

    @Override
    public String getdeclContent(Long did) {
        return adminMapper.getdeclContent(did);
    }

    @Override
    public void rejectRequest(StoreReject reject) {
        adminMapper.rejectRequest(reject);
    }


    @Override
    public List<StoreReject>  getStoreRejectByRequestId(Long requestId) {
        return adminMapper.getStoreRejectByRequestId(requestId);
    }

    @Override
    public void updateRequestStatus(StoreRequestVO requestById) {
       adminMapper.updateRequestStatus(requestById);
    }
}
