import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ExpenseTypeFormService } from './expense-type-form.service';
import { ExpenseTypeService } from '../service/expense-type.service';
import { IExpenseType } from '../expense-type.model';

import { ExpenseTypeUpdateComponent } from './expense-type-update.component';

describe('ExpenseType Management Update Component', () => {
  let comp: ExpenseTypeUpdateComponent;
  let fixture: ComponentFixture<ExpenseTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let expenseTypeFormService: ExpenseTypeFormService;
  let expenseTypeService: ExpenseTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ExpenseTypeUpdateComponent],
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
      .overrideTemplate(ExpenseTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExpenseTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    expenseTypeFormService = TestBed.inject(ExpenseTypeFormService);
    expenseTypeService = TestBed.inject(ExpenseTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const expenseType: IExpenseType = { id: 456 };

      activatedRoute.data = of({ expenseType });
      comp.ngOnInit();

      expect(comp.expenseType).toEqual(expenseType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenseType>>();
      const expenseType = { id: 123 };
      jest.spyOn(expenseTypeFormService, 'getExpenseType').mockReturnValue(expenseType);
      jest.spyOn(expenseTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenseType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expenseType }));
      saveSubject.complete();

      // THEN
      expect(expenseTypeFormService.getExpenseType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(expenseTypeService.update).toHaveBeenCalledWith(expect.objectContaining(expenseType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenseType>>();
      const expenseType = { id: 123 };
      jest.spyOn(expenseTypeFormService, 'getExpenseType').mockReturnValue({ id: null });
      jest.spyOn(expenseTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenseType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expenseType }));
      saveSubject.complete();

      // THEN
      expect(expenseTypeFormService.getExpenseType).toHaveBeenCalled();
      expect(expenseTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenseType>>();
      const expenseType = { id: 123 };
      jest.spyOn(expenseTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenseType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(expenseTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
