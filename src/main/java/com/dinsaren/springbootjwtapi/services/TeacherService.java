package com.dinsaren.springbootjwtapi.services;

import com.dinsaren.springbootjwtapi.models.req.TeacherReq;
import com.dinsaren.springbootjwtapi.models.res.TeacherRes;

import java.util.List;

public interface TeacherService {

    TeacherRes create(TeacherReq request);

    TeacherRes update(Integer id, TeacherReq request);

    TeacherRes findById(Integer id);

    List<TeacherRes> findAll();

    void delete(Integer id);

}