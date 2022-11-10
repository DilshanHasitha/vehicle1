import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ExpenseAccountBalanceFormService } from './expense-account-balance-form.service';
import { ExpenseAccountBalanceService } from '../service/expense-account-balance.service';
import { IExpenseAccountBalance } from '../expense-account-balance.model';
import { IExpense } from 'app/entities/expense/expense.model';
import { ExpenseService } from 'app/entities/expense/service/expense.service';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { MerchantService } from 'app/entities/merchant/service/merchant.service';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/service/transaction-type.service';

import { ExpenseAccountBalanceUpdateComponent } from './expense-account-balance-update.component';

describe('ExpenseAccountBalance Management Update Component', () => {
  let comp: ExpenseAccountBalanceUpdateComponent;
  let fixture: ComponentFixture<ExpenseAccountBalanceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let expenseAccountBalanceFormService: ExpenseAccountBalanceFormService;
  let expenseAccountBalanceService: ExpenseAccountBalanceService;
  let expenseService: ExpenseService;
  let merchantService: MerchantService;
  let transactionTypeService: TransactionTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ExpenseAccountBalanceUpdateComponent],
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
      .overrideTemplate(ExpenseAccountBalanceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExpenseAccountBalanceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    expenseAccountBalanceFormService = TestBed.inject(ExpenseAccountBalanceFormService);
    expenseAccountBalanceService = TestBed.inject(ExpenseAccountBalanceService);
    expenseService = TestBed.inject(ExpenseService);
    merchantService = TestBed.inject(MerchantService);
    transactionTypeService = TestBed.inject(TransactionTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Expense query and add missing value', () => {
      const expenseAccountBalance: IExpenseAccountBalance = { id: 456 };
      const expense: IExpense = { id: 53936 };
      expenseAccountBalance.expense = expense;

      const expenseCollection: IExpense[] = [{ id: 2126 }];
      jest.spyOn(expenseService, 'query').mockReturnValue(of(new HttpResponse({ body: expenseCollection })));
      const additionalExpenses = [expense];
      const expectedCollection: IExpense[] = [...additionalExpenses, ...expenseCollection];
      jest.spyOn(expenseService, 'addExpenseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expenseAccountBalance });
      comp.ngOnInit();

      expect(expenseService.query).toHaveBeenCalled();
      expect(expenseService.addExpenseToCollectionIfMissing).toHaveBeenCalledWith(
        expenseCollection,
        ...additionalExpenses.map(expect.objectContaining)
      );
      expect(comp.expensesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Merchant query and add missing value', () => {
      const expenseAccountBalance: IExpenseAccountBalance = { id: 456 };
      const merchant: IMerchant = { id: 47940 };
      expenseAccountBalance.merchant = merchant;

      const merchantCollection: IMerchant[] = [{ id: 27229 }];
      jest.spyOn(merchantService, 'query').mockReturnValue(of(new HttpResponse({ body: merchantCollection })));
      const additionalMerchants = [merchant];
      const expectedCollection: IMerchant[] = [...additionalMerchants, ...merchantCollection];
      jest.spyOn(merchantService, 'addMerchantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expenseAccountBalance });
      comp.ngOnInit();

      expect(merchantService.query).toHaveBeenCalled();
      expect(merchantService.addMerchantToCollectionIfMissing).toHaveBeenCalledWith(
        merchantCollection,
        ...additionalMerchants.map(expect.objectContaining)
      );
      expect(comp.merchantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TransactionType query and add missing value', () => {
      const expenseAccountBalance: IExpenseAccountBalance = { id: 456 };
      const transactionType: ITransactionType = { id: 65049 };
      expenseAccountBalance.transactionType = transactionType;

      const transactionTypeCollection: ITransactionType[] = [{ id: 3845 }];
      jest.spyOn(transactionTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionTypeCollection })));
      const additionalTransactionTypes = [transactionType];
      const expectedCollection: ITransactionType[] = [...additionalTransactionTypes, ...transactionTypeCollection];
      jest.spyOn(transactionTypeService, 'addTransactionTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expenseAccountBalance });
      comp.ngOnInit();

      expect(transactionTypeService.query).toHaveBeenCalled();
      expect(transactionTypeService.addTransactionTypeToCollectionIfMissing).toHaveBeenCalledWith(
        transactionTypeCollection,
        ...additionalTransactionTypes.map(expect.objectContaining)
      );
      expect(comp.transactionTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const expenseAccountBalance: IExpenseAccountBalance = { id: 456 };
      const expense: IExpense = { id: 58702 };
      expenseAccountBalance.expense = expense;
      const merchant: IMerchant = { id: 91538 };
      expenseAccountBalance.merchant = merchant;
      const transactionType: ITransactionType = { id: 71786 };
      expenseAccountBalance.transactionType = transactionType;

      activatedRoute.data = of({ expenseAccountBalance });
      comp.ngOnInit();

      expect(comp.expensesSharedCollection).toContain(expense);
      expect(comp.merchantsSharedCollection).toContain(merchant);
      expect(comp.transactionTypesSharedCollection).toContain(transactionType);
      expect(comp.expenseAccountBalance).toEqual(expenseAccountBalance);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenseAccountBalance>>();
      const expenseAccountBalance = { id: 123 };
      jest.spyOn(expenseAccountBalanceFormService, 'getExpenseAccountBalance').mockReturnValue(expenseAccountBalance);
      jest.spyOn(expenseAccountBalanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenseAccountBalance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expenseAccountBalance }));
      saveSubject.complete();

      // THEN
      expect(expenseAccountBalanceFormService.getExpenseAccountBalance).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(expenseAccountBalanceService.update).toHaveBeenCalledWith(expect.objectContaining(expenseAccountBalance));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenseAccountBalance>>();
      const expenseAccountBalance = { id: 123 };
      jest.spyOn(expenseAccountBalanceFormService, 'getExpenseAccountBalance').mockReturnValue({ id: null });
      jest.spyOn(expenseAccountBalanceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenseAccountBalance: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expenseAccountBalance }));
      saveSubject.complete();

      // THEN
      expect(expenseAccountBalanceFormService.getExpenseAccountBalance).toHaveBeenCalled();
      expect(expenseAccountBalanceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenseAccountBalance>>();
      const expenseAccountBalance = { id: 123 };
      jest.spyOn(expenseAccountBalanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenseAccountBalance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(expenseAccountBalanceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareExpense', () => {
      it('Should forward to expenseService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(expenseService, 'compareExpense');
        comp.compareExpense(entity, entity2);
        expect(expenseService.compareExpense).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMerchant', () => {
      it('Should forward to merchantService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(merchantService, 'compareMerchant');
        comp.compareMerchant(entity, entity2);
        expect(merchantService.compareMerchant).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTransactionType', () => {
      it('Should forward to transactionTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(transactionTypeService, 'compareTransactionType');
        comp.compareTransactionType(entity, entity2);
        expect(transactionTypeService.compareTransactionType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
