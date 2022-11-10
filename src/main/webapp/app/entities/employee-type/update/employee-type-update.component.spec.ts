import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeeTypeFormService } from './employee-type-form.service';
import { EmployeeTypeService } from '../service/employee-type.service';
import { IEmployeeType } from '../employee-type.model';

import { EmployeeTypeUpdateComponent } from './employee-type-update.component';

describe('EmployeeType Management Update Component', () => {
  let comp: EmployeeTypeUpdateComponent;
  let fixture: ComponentFixture<EmployeeTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeTypeFormService: EmployeeTypeFormService;
  let employeeTypeService: EmployeeTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeeTypeUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EmployeeTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeTypeFormService = TestBed.inject(EmployeeTypeFormService);
    employeeTypeService = TestBed.inject(EmployeeTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const employeeType: IEmployeeType = { id: 456 };

      activatedRoute.data = of({ employeeType });
      comp.ngOnInit();

      expect(comp.employeeType).toEqual(employeeType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeType>>();
      const employeeType = { id: 123 };
      jest.spyOn(employeeTypeFormService, 'getEmployeeType').mockReturnValue(employeeType);
      jest.spyOn(employeeTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeType }));
      saveSubject.complete();

      // THEN
      expect(employeeTypeFormService.getEmployeeType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeTypeService.update).toHaveBeenCalledWith(expect.objectContaining(employeeType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeType>>();
      const employeeType = { id: 123 };
      jest.spyOn(employeeTypeFormService, 'getEmployeeType').mockReturnValue({ id: null });
      jest.spyOn(employeeTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeType }));
      saveSubject.complete();

      // THEN
      expect(employeeTypeFormService.getEmployeeType).toHaveBeenCalled();
      expect(employeeTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeType>>();
      const employeeType = { id: 123 };
      jest.spyOn(employeeTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
