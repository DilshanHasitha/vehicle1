import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ExpenseAccountFormService } from './expense-account-form.service';
import { ExpenseAccountService } from '../service/expense-account.service';
import { IExpenseAccount } from '../expense-account.model';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/service/transaction-type.service';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { MerchantService } from 'app/entities/merchant/service/merchant.service';
import { IExpense } from 'app/entities/expense/expense.model';
import { ExpenseService } from 'app/entities/expense/service/expense.service';

import { ExpenseAccountUpdateComponent } from './expense-account-update.component';

describe('ExpenseAccount Management Update Component', () => {
  let comp: ExpenseAccountUpdateComponent;
  let fixture: ComponentFixture<ExpenseAccountUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let expenseAccountFormService: ExpenseAccountFormService;
  let expenseAccountService: ExpenseAccountService;
  let transactionTypeService: TransactionTypeService;
  let merchantService: MerchantService;
  let expenseService: ExpenseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ExpenseAccountUpdateComponent],
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
      .overrideTemplate(ExpenseAccountUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExpenseAccountUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    expenseAccountFormService = TestBed.inject(ExpenseAccountFormService);
    expenseAccountService = TestBed.inject(ExpenseAccountService);
    transactionTypeService = TestBed.inject(TransactionTypeService);
    merchantService = TestBed.inject(MerchantService);
    expenseService = TestBed.inject(ExpenseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TransactionType query and add missing value', () => {
      const expenseAccount: IExpenseAccount = { id: 456 };
      const transactionType: ITransactionType = { id: 64138 };
      expenseAccount.transactionType = transactionType;

      const transactionTypeCollection: ITransactionType[] = [{ id: 2006 }];
      jest.spyOn(transactionTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionTypeCollection })));
      const additionalTransactionTypes = [transactionType];
      const expectedCollection: ITransactionType[] = [...additionalTransactionTypes, ...transactionTypeCollection];
      jest.spyOn(transactionTypeService, 'addTransactionTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expenseAccount });
      comp.ngOnInit();

      expect(transactionTypeService.query).toHaveBeenCalled();
      expect(transactionTypeService.addTransactionTypeToCollectionIfMissing).toHaveBeenCalledWith(
        transactionTypeCollection,
        ...additionalTransactionTypes.map(expect.objectContaining)
      );
      expect(comp.transactionTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Merchant query and add missing value', () => {
      const expenseAccount: IExpenseAccount = { id: 456 };
      const merchant: IMerchant = { id: 11560 };
      expenseAccount.merchant = merchant;

      const merchantCollection: IMerchant[] = [{ id: 18257 }];
      jest.spyOn(merchantService, 'query').mockReturnValue(of(new HttpResponse({ body: merchantCollection })));
      const additionalMerchants = [merchant];
      const expectedCollection: IMerchant[] = [...additionalMerchants, ...merchantCollection];
      jest.spyOn(merchantService, 'addMerchantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expenseAccount });
      comp.ngOnInit();

      expect(merchantService.query).toHaveBeenCalled();
      expect(merchantService.addMerchantToCollectionIfMissing).toHaveBeenCalledWith(
        merchantCollection,
        ...additionalMerchants.map(expect.objectContaining)
      );
      expect(comp.merchantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Expense query and add missing value', () => {
      const expenseAccount: IExpenseAccount = { id: 456 };
      const expense: IExpense = { id: 21386 };
      expenseAccount.expense = expense;

      const expenseCollection: IExpense[] = [{ id: 13063 }];
      jest.spyOn(expenseService, 'query').mockReturnValue(of(new HttpResponse({ body: expenseCollection })));
      const additionalExpenses = [expense];
      const expectedCollection: IExpense[] = [...additionalExpenses, ...expenseCollection];
      jest.spyOn(expenseService, 'addExpenseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expenseAccount });
      comp.ngOnInit();

      expect(expenseService.query).toHaveBeenCalled();
      expect(expenseService.addExpenseToCollectionIfMissing).toHaveBeenCalledWith(
        expenseCollection,
        ...additionalExpenses.map(expect.objectContaining)
      );
      expect(comp.expensesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const expenseAccount: IExpenseAccount = { id: 456 };
      const transactionType: ITransactionType = { id: 18804 };
      expenseAccount.transactionType = transactionType;
      const merchant: IMerchant = { id: 20278 };
      expenseAccount.merchant = merchant;
      const expense: IExpense = { id: 80275 };
      expenseAccount.expense = expense;

      activatedRoute.data = of({ expenseAccount });
      comp.ngOnInit();

      expect(comp.transactionTypesSharedCollection).toContain(transactionType);
      expect(comp.merchantsSharedCollection).toContain(merchant);
      expect(comp.expensesSharedCollection).toContain(expense);
      expect(comp.expenseAccount).toEqual(expenseAccount);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenseAccount>>();
      const expenseAccount = { id: 123 };
      jest.spyOn(expenseAccountFormService, 'getExpenseAccount').mockReturnValue(expenseAccount);
      jest.spyOn(expenseAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenseAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expenseAccount }));
      saveSubject.complete();

      // THEN
      expect(expenseAccountFormService.getExpenseAccount).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(expenseAccountService.update).toHaveBeenCalledWith(expect.objectContaining(expenseAccount));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenseAccount>>();
      const expenseAccount = { id: 123 };
      jest.spyOn(expenseAccountFormService, 'getExpenseAccount').mockReturnValue({ id: null });
      jest.spyOn(expenseAccountService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenseAccount: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expenseAccount }));
      saveSubject.complete();

      // THEN
      expect(expenseAccountFormService.getExpenseAccount).toHaveBeenCalled();
      expect(expenseAccountService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenseAccount>>();
      const expenseAccount = { id: 123 };
      jest.spyOn(expenseAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenseAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(expenseAccountService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTransactionType', () => {
      it('Should forward to transactionTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(transactionTypeService, 'compareTransactionType');
        comp.compareTransactionType(entity, entity2);
        expect(transactionTypeService.compareTransactionType).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareExpense', () => {
      it('Should forward to expenseService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(expenseService, 'compareExpense');
        comp.compareExpense(entity, entity2);
        expect(expenseService.compareExpense).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
