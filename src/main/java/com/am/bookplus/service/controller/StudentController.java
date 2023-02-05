package com.am.bookplus.service.controller;

import com.am.bookplus.service.entity.EntityStudent;
import com.am.bookplus.service.Response;
import com.am.bookplus.service.Utils;
import com.am.bookplus.service.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/student")
@Transactional
public class StudentController
{
    @Autowired
    private StudentRepository studentRepository;

    @GetMapping(path = "/list")
    public @ResponseBody Response getStudents(@RequestParam int page, @RequestParam int size)
    {
        Pageable limit = PageRequest.of(page,size);
        return new Response(Response.ResultCode.RESULT_OK, Response.Request.GET_STUDENTS, studentRepository.findAll(limit));
    }
    
    @GetMapping
    public @ResponseBody Response findStudent(@RequestParam String id)
    {
        Optional<EntityStudent> optionalEntityStudent = studentRepository.findById(id);
        if (optionalEntityStudent.isEmpty())
            return new Response(Response.ResultCode.RESULT_STUDENT_NOT_EXISTS, Response.Request.FIND_STUDENT);
        
        EntityStudent student = optionalEntityStudent.get();
        return new Response(Response.ResultCode.RESULT_OK, Response.Request.FIND_STUDENT, student);
    }

    @PostMapping
    public @ResponseBody Response addStudent(@RequestBody EntityStudent student)
    {
        if (studentRepository.findById(student.getId()).isPresent())
            return new Response(Response.ResultCode.RESULT_STUDENT_ALREADY_EXISTS, Response.Request.ADD_STUDENT);
        
        if (!Utils.checkStudentId(student.getId()))
            return new Response(Response.ResultCode.RESULT_ID_NOT_VALID, Response.Request.ADD_STUDENT);
        
        if (!Utils.checkEmail(student.getEmail()))
            return new Response(Response.ResultCode.RESULT_EMAIL_NOT_VALID, Response.Request.ADD_STUDENT);
        
        studentRepository.save(student);
        return new Response(Response.ResultCode.RESULT_OK, Response.Request.ADD_STUDENT);
    }

    @DeleteMapping
    public @ResponseBody Response deleteStudent(@RequestParam String id)
    {
        Optional<EntityStudent> optionalEntityStudent = studentRepository.findById(id);
        if (optionalEntityStudent.isEmpty())
            return new Response(Response.ResultCode.RESULT_STUDENT_NOT_EXISTS, Response.Request.DELETE_STUDENT);
        
        EntityStudent student = optionalEntityStudent.get();
        if (!student.getRegisterOccurrences().isEmpty())
            return new Response(Response.ResultCode.RESULT_CANT_DELETE_STUDENT, Response.Request.DELETE_STUDENT);

        studentRepository.deleteById(id);
        return new Response(Response.ResultCode.RESULT_OK, Response.Request.DELETE_STUDENT);
    }

    @PostMapping(path = "/update")
    public @ResponseBody Response updateStudent(@RequestBody EntityStudent update)
    {
        Optional<EntityStudent> optionalEntityStudent = studentRepository.findById(update.getId());
        if (optionalEntityStudent.isEmpty())
            return new Response(Response.ResultCode.RESULT_STUDENT_NOT_EXISTS, Response.Request.UPDATE_STUDENT);

        EntityStudent student = optionalEntityStudent.get();
        student.setName(update.getName());
        student.setSurname(update.getSurname());
        student.setEmail(update.getEmail());

        studentRepository.save(student);
        return new Response(Response.ResultCode.RESULT_OK, Response.Request.UPDATE_STUDENT);
    }
}
