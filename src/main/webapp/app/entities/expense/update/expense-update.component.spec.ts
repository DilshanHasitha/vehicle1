import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ExpenseFormService } from './expense-form.service';
import { ExpenseService } from '../service/expense.service';
import { IExpense } from '../expense.model';

import { ExpenseUpdateComponent } from './expense-update.component';

describe('Expense Management Update Component', () => {
  let comp: ExpenseUpdateComponent;
  let fixture: ComponentFixture<ExpenseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let expenseFormService: ExpenseFormService;
  let expenseService: ExpenseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ExpenseUpdateComponent],
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
      .overrideTemplate(ExpenseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExpenseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    expenseFormService = TestBed.inject(ExpenseFormService);
    expenseService = TestBed.inject(ExpenseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const expense: IExpense = { id: 456 };

      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      expect(comp.expense).toEqual(expense);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpense>>();
      const expense = { id: 123 };
      jest.spyOn(expenseFormService, 'getExpense').mockReturnValue(expense);
      jest.spyOn(expenseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expense }));
      saveSubject.complete();

      // THEN
      expect(expenseFormService.getExpense).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(expenseService.update).toHaveBeenCalledWith(expect.objectContaining(expense));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpense>>();
      const expense = { id: 123 };
      jest.spyOn(expenseFormService, 'getExpense').mockReturnValue({ id: null });
      jest.spyOn(expenseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expense: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expense }));
      saveSubject.complete();

      // THEN
      expect(expenseFormService.getExpense).toHaveBeenCalled();
      expect(expenseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpense>>();
      const expense = { id: 123 };
      jest.spyOn(expenseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(expenseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
