import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employee-account.test-samples';

import { EmployeeAccountFormService } from './employee-account-form.service';

describe('EmployeeAccount Form Service', () => {
  let service: EmployeeAccountFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeeAccountFormService);
  });

  describe('Service methods', () => {
    describe('createEmployeeAccountFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmployeeAccountFormGroup();

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
            employee: expect.any(Object),
          })
        );
      });

      it('passing IEmployeeAccount should create a new form with FormGroup', () => {
        const formGroup = service.createEmployeeAccountFormGroup(sampleWithRequiredData);

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
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getEmployeeAccount', () => {
      it('should return NewEmployeeAccount for default EmployeeAccount initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEmployeeAccountFormGroup(sampleWithNewData);

        const employeeAccount = service.getEmployeeAccount(formGroup) as any;

        expect(employeeAccount).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmployeeAccount for empty EmployeeAccount initial value', () => {
        const formGroup = service.createEmployeeAccountFormGroup();

        const employeeAccount = service.getEmployeeAccount(formGroup) as any;

        expect(employeeAccount).toMatchObject({});
      });

      it('should return IEmployeeAccount', () => {
        const formGroup = service.createEmployeeAccountFormGroup(sampleWithRequiredData);

        const employeeAccount = service.getEmployeeAccount(formGroup) as any;

        expect(employeeAccount).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmployeeAccount should not enable id FormControl', () => {
        const formGroup = service.createEmployeeAccountFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmployeeAccount should disable id FormControl', () => {
        const formGroup = service.createEmployeeAccountFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
