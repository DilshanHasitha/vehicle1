import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../cash-book-balance.test-samples';

import { CashBookBalanceFormService } from './cash-book-balance-form.service';

describe('CashBookBalance Form Service', () => {
  let service: CashBookBalanceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CashBookBalanceFormService);
  });

  describe('Service methods', () => {
    describe('createCashBookBalanceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCashBookBalanceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            balance: expect.any(Object),
            merchant: expect.any(Object),
            transactionType: expect.any(Object),
          })
        );
      });

      it('passing ICashBookBalance should create a new form with FormGroup', () => {
        const formGroup = service.createCashBookBalanceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            balance: expect.any(Object),
            merchant: expect.any(Object),
            transactionType: expect.any(Object),
          })
        );
      });
    });

    describe('getCashBookBalance', () => {
      it('should return NewCashBookBalance for default CashBookBalance initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCashBookBalanceFormGroup(sampleWithNewData);

        const cashBookBalance = service.getCashBookBalance(formGroup) as any;

        expect(cashBookBalance).toMatchObject(sampleWithNewData);
      });

      it('should return NewCashBookBalance for empty CashBookBalance initial value', () => {
        const formGroup = service.createCashBookBalanceFormGroup();

        const cashBookBalance = service.getCashBookBalance(formGroup) as any;

        expect(cashBookBalance).toMatchObject({});
      });

      it('should return ICashBookBalance', () => {
        const formGroup = service.createCashBookBalanceFormGroup(sampleWithRequiredData);

        const cashBookBalance = service.getCashBookBalance(formGroup) as any;

        expect(cashBookBalance).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICashBookBalance should not enable id FormControl', () => {
        const formGroup = service.createCashBookBalanceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCashBookBalance should disable id FormControl', () => {
        const formGroup = service.createCashBookBalanceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
