package com.dinsaren.springbootjwtapi.services;

import com.dinsaren.springbootjwtapi.models.req.StudentReq;
import com.dinsaren.springbootjwtapi.models.res.StudentRes;

import java.util.List;

public interface StudentService {

    StudentRes create(StudentReq request);

    StudentRes update(Integer id, StudentReq request);

    StudentRes findById(Integer id);

    List<StudentRes> findAll();

    void delete(Integer id);
}