import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../cash-book.test-samples';

import { CashBookFormService } from './cash-book-form.service';

describe('CashBook Form Service', () => {
  let service: CashBookFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CashBookFormService);
  });

  describe('Service methods', () => {
    describe('createCashBookFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCashBookFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            transactionDate: expect.any(Object),
            transactionDescription: expect.any(Object),
            transactionAmountDR: expect.any(Object),
            transactionAmountCR: expect.any(Object),
            transactionBalance: expect.any(Object),
            merchant: expect.any(Object),
            transactionType: expect.any(Object),
          })
        );
      });

      it('passing ICashBook should create a new form with FormGroup', () => {
        const formGroup = service.createCashBookFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            transactionDate: expect.any(Object),
            transactionDescription: expect.any(Object),
            transactionAmountDR: expect.any(Object),
            transactionAmountCR: expect.any(Object),
            transactionBalance: expect.any(Object),
            merchant: expect.any(Object),
            transactionType: expect.any(Object),
          })
        );
      });
    });

    describe('getCashBook', () => {
      it('should return NewCashBook for default CashBook initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCashBookFormGroup(sampleWithNewData);

        const cashBook = service.getCashBook(formGroup) as any;

        expect(cashBook).toMatchObject(sampleWithNewData);
      });

      it('should return NewCashBook for empty CashBook initial value', () => {
        const formGroup = service.createCashBookFormGroup();

        const cashBook = service.getCashBook(formGroup) as any;

        expect(cashBook).toMatchObject({});
      });

      it('should return ICashBook', () => {
        const formGroup = service.createCashBookFormGroup(sampleWithRequiredData);

        const cashBook = service.getCashBook(formGroup) as any;

        expect(cashBook).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICashBook should not enable id FormControl', () => {
        const formGroup = service.createCashBookFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCashBook should disable id FormControl', () => {
        const formGroup = service.createCashBookFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
