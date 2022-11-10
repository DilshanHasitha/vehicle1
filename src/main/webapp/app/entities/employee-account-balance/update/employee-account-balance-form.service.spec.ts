import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employee-account-balance.test-samples';

import { EmployeeAccountBalanceFormService } from './employee-account-balance-form.service';

describe('EmployeeAccountBalance Form Service', () => {
  let service: EmployeeAccountBalanceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeeAccountBalanceFormService);
  });

  describe('Service methods', () => {
    describe('createEmployeeAccountBalanceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmployeeAccountBalanceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            balance: expect.any(Object),
            employee: expect.any(Object),
            merchant: expect.any(Object),
            transactionType: expect.any(Object),
          })
        );
      });

      it('passing IEmployeeAccountBalance should create a new form with FormGroup', () => {
        const formGroup = service.createEmployeeAccountBalanceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            balance: expect.any(Object),
            employee: expect.any(Object),
            merchant: expect.any(Object),
            transactionType: expect.any(Object),
          })
        );
      });
    });

    describe('getEmployeeAccountBalance', () => {
      it('should return NewEmployeeAccountBalance for default EmployeeAccountBalance initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEmployeeAccountBalanceFormGroup(sampleWithNewData);

        const employeeAccountBalance = service.getEmployeeAccountBalance(formGroup) as any;

        expect(employeeAccountBalance).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmployeeAccountBalance for empty EmployeeAccountBalance initial value', () => {
        const formGroup = service.createEmployeeAccountBalanceFormGroup();

        const employeeAccountBalance = service.getEmployeeAccountBalance(formGroup) as any;

        expect(employeeAccountBalance).toMatchObject({});
      });

      it('should return IEmployeeAccountBalance', () => {
        const formGroup = service.createEmployeeAccountBalanceFormGroup(sampleWithRequiredData);

        const employeeAccountBalance = service.getEmployeeAccountBalance(formGroup) as any;

        expect(employeeAccountBalance).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmployeeAccountBalance should not enable id FormControl', () => {
        const formGroup = service.createEmployeeAccountBalanceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmployeeAccountBalance should disable id FormControl', () => {
        const formGroup = service.createEmployeeAccountBalanceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
