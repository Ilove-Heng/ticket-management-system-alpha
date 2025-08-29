package com.nangseakheng.user.service;


import com.nangseakheng.common.criteria.BaseSearchCriteria;
import com.nangseakheng.common.dto.PageableRequestVO;
import com.nangseakheng.common.dto.PageableResponseVO;
import com.nangseakheng.user.dto.request.UserFilterRequest;
import com.nangseakheng.user.entity.User;

public interface UserSearchService {
    PageableResponseVO<User> searchUsers(UserFilterRequest filterRequest);

    PageableResponseVO<User> searchUsersWithCriteria(BaseSearchCriteria searchCriteria, PageableRequestVO pageable);
}