import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../expense-account.test-samples';

import { ExpenseAccountFormService } from './expense-account-form.service';

describe('ExpenseAccount Form Service', () => {
  let service: ExpenseAccountFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExpenseAccountFormService);
  });

  describe('Service methods', () => {
    describe('createExpenseAccountFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExpenseAccountFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            transactionDate: expect.any(Object),
            transactionDescription: expect.any(Object),
            transactionAmountDR: expect.any(Object),
            transactionAmountCR: expect.any(Object),
            transactionBalance: expect.any(Object),
            transactionType: expect.any(Object),
            merchant: expect.any(Object),
            expense: expect.any(Object),
          })
        );
      });

      it('passing IExpenseAccount should create a new form with FormGroup', () => {
        const formGroup = service.createExpenseAccountFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            transactionDate: expect.any(Object),
            transactionDescription: expect.any(Object),
            transactionAmountDR: expect.any(Object),
            transactionAmountCR: expect.any(Object),
            transactionBalance: expect.any(Object),
            transactionType: expect.any(Object),
            merchant: expect.any(Object),
            expense: expect.any(Object),
          })
        );
      });
    });

    describe('getExpenseAccount', () => {
      it('should return NewExpenseAccount for default ExpenseAccount initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createExpenseAccountFormGroup(sampleWithNewData);

        const expenseAccount = service.getExpenseAccount(formGroup) as any;

        expect(expenseAccount).toMatchObject(sampleWithNewData);
      });

      it('should return NewExpenseAccount for empty ExpenseAccount initial value', () => {
        const formGroup = service.createExpenseAccountFormGroup();

        const expenseAccount = service.getExpenseAccount(formGroup) as any;

        expect(expenseAccount).toMatchObject({});
      });

      it('should return IExpenseAccount', () => {
        const formGroup = service.createExpenseAccountFormGroup(sampleWithRequiredData);

        const expenseAccount = service.getExpenseAccount(formGroup) as any;

        expect(expenseAccount).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExpenseAccount should not enable id FormControl', () => {
        const formGroup = service.createExpenseAccountFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExpenseAccount should disable id FormControl', () => {
        const formGroup = service.createExpenseAccountFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
