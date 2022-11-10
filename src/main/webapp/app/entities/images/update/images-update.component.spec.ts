import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ImagesFormService } from './images-form.service';
import { ImagesService } from '../service/images.service';
import { IImages } from '../images.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { ImagesUpdateComponent } from './images-update.component';

describe('Images Management Update Component', () => {
  let comp: ImagesUpdateComponent;
  let fixture: ComponentFixture<ImagesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let imagesFormService: ImagesFormService;
  let imagesService: ImagesService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ImagesUpdateComponent],
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
      .overrideTemplate(ImagesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImagesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    imagesFormService = TestBed.inject(ImagesFormService);
    imagesService = TestBed.inject(ImagesService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const images: IImages = { id: 456 };
      const employee: IEmployee = { id: 19597 };
      images.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 15638 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ images });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const images: IImages = { id: 456 };
      const employee: IEmployee = { id: 86311 };
      images.employee = employee;

      activatedRoute.data = of({ images });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.images).toEqual(images);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImages>>();
      const images = { id: 123 };
      jest.spyOn(imagesFormService, 'getImages').mockReturnValue(images);
      jest.spyOn(imagesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ images });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: images }));
      saveSubject.complete();

      // THEN
      expect(imagesFormService.getImages).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(imagesService.update).toHaveBeenCalledWith(expect.objectContaining(images));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImages>>();
      const images = { id: 123 };
      jest.spyOn(imagesFormService, 'getImages').mockReturnValue({ id: null });
      jest.spyOn(imagesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ images: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: images }));
      saveSubject.complete();

      // THEN
      expect(imagesFormService.getImages).toHaveBeenCalled();
      expect(imagesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImages>>();
      const images = { id: 123 };
      jest.spyOn(imagesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ images });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(imagesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
