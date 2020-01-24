package com.java.workshop.controller;

import com.java.workshop.exception.MyConflictException;
import com.java.workshop.exception.MyNotFoundException;
import com.java.workshop.model.workshop.WorkshopSession;
import com.java.workshop.model.workshop.form.*;
import com.java.workshop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/forms")
public class FormController {
    @Autowired
    private FormRepository formRepository;

    @Autowired
    private GroupFilledFormRepository groupFilledFormRepository;

    @Autowired
    private StudentFilledFormRepository studentFilledFormRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GroupAnswerRepository groupAnswerRepository;

    @Autowired
    private StudentAnswerRepository studentAnswerRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/all")
    public ArrayList<Form> allForms() {
        return (ArrayList<Form>) formRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Form> form(@PathVariable Long id) {
        if (!formRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(formRepository.findById(id).get(), HttpStatus.OK);
    }

    @PostMapping("/create/{type}/{session_id}")
    public ResponseEntity<Form> createForm(@PathVariable FormType type, @PathVariable Long session_id, @RequestBody List<String> strings) {
        if (!sessionRepository.existsById(session_id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Form form = new Form();
        form.setFormType(type);
        WorkshopSession session = sessionRepository.findById(session_id).get();
        form.setWorkshopSession(session);
        form = formRepository.save(form);
        for (String s : strings) {
            Question question = new Question();
            question.setText(s);
            question.setForm(form);
            questionRepository.save(question);
        }
        return new ResponseEntity<>(form, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/fill_group/{gid}")
    public ResponseEntity<GroupFilledForm> fillGroup(@PathVariable Long id, @PathVariable Long gid, @RequestBody List<String> strings) {

        if (!formRepository.existsById(id)) {
            throw new MyNotFoundException("Form not found.");
        }
        if (!groupRepository.existsById(gid)) {
            throw new MyNotFoundException("Group not found.");
        }
        if (!formRepository.findById(id).get().getFormType().equals(FormType.GROUP)) {
            throw new MyConflictException("This form can't be filled for a group.");
        }
        if (formRepository.findById(id).get().getQuestions().size() != strings.size()) {
            throw new MyConflictException("Number of answers should be equal to number of questions.");
        }
        GroupFilledForm gFForm = new GroupFilledForm();
        gFForm.setForm(formRepository.findById(id).get());
        gFForm.setGroup(groupRepository.findById(gid).get());
        gFForm.setDateCreated(new Date());
        gFForm = groupFilledFormRepository.save(gFForm);
        for (String s : strings) {
            GroupAnswer answer = new GroupAnswer();
            answer.setText(s);
            answer.setGroupFilledForm(gFForm);
            groupAnswerRepository.save(answer);
        }
        return new ResponseEntity<>(gFForm, HttpStatus.OK);
    }

    @PostMapping("/{id}/fill_student/{sid}")
    public ResponseEntity<StudentFilledForm> fillStuedent(@PathVariable Long id, @PathVariable Long sid, @RequestBody List<String> strings) {

        if (!formRepository.existsById(id)) {
            throw new MyNotFoundException("Form not found.");
        }
        if (!studentRepository.existsById(sid)) {
            throw new MyNotFoundException("Student not found.");
        }
        if (!formRepository.findById(id).get().getFormType().equals(FormType.INDIVIDUAL)) {
            throw new MyConflictException("This form can't be filled for a student.");
        }
        if (formRepository.findById(id).get().getQuestions().size() != strings.size()) {
            throw new MyConflictException("Number of answers should be equal to number of questions.");
        }
        StudentFilledForm sFForm = new StudentFilledForm();
        sFForm.setForm(formRepository.findById(id).get());
        sFForm.setStudent(studentRepository.findById(sid).get());
        sFForm.setDateCreated(new Date());
        sFForm = studentFilledFormRepository.save(sFForm);
        for (String s : strings) {
            StudentAnswer answer = new StudentAnswer();
            answer.setText(s);
            answer.setStudentFilledForm(sFForm);
            studentAnswerRepository.save(answer);
        }
        return new ResponseEntity<>(sFForm, HttpStatus.OK);
    }
}
