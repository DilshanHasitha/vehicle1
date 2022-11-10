import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../expense-account-balance.test-samples';

import { ExpenseAccountBalanceFormService } from './expense-account-balance-form.service';

describe('ExpenseAccountBalance Form Service', () => {
  let service: ExpenseAccountBalanceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExpenseAccountBalanceFormService);
  });

  describe('Service methods', () => {
    describe('createExpenseAccountBalanceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExpenseAccountBalanceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            balance: expect.any(Object),
            expense: expect.any(Object),
            merchant: expect.any(Object),
            transactionType: expect.any(Object),
          })
        );
      });

      it('passing IExpenseAccountBalance should create a new form with FormGroup', () => {
        const formGroup = service.createExpenseAccountBalanceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            balance: expect.any(Object),
            expense: expect.any(Object),
            merchant: expect.any(Object),
            transactionType: expect.any(Object),
          })
        );
      });
    });

    describe('getExpenseAccountBalance', () => {
      it('should return NewExpenseAccountBalance for default ExpenseAccountBalance initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createExpenseAccountBalanceFormGroup(sampleWithNewData);

        const expenseAccountBalance = service.getExpenseAccountBalance(formGroup) as any;

        expect(expenseAccountBalance).toMatchObject(sampleWithNewData);
      });

      it('should return NewExpenseAccountBalance for empty ExpenseAccountBalance initial value', () => {
        const formGroup = service.createExpenseAccountBalanceFormGroup();

        const expenseAccountBalance = service.getExpenseAccountBalance(formGroup) as any;

        expect(expenseAccountBalance).toMatchObject({});
      });

      it('should return IExpenseAccountBalance', () => {
        const formGroup = service.createExpenseAccountBalanceFormGroup(sampleWithRequiredData);

        const expenseAccountBalance = service.getExpenseAccountBalance(formGroup) as any;

        expect(expenseAccountBalance).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExpenseAccountBalance should not enable id FormControl', () => {
        const formGroup = service.createExpenseAccountBalanceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExpenseAccountBalance should disable id FormControl', () => {
        const formGroup = service.createExpenseAccountBalanceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
