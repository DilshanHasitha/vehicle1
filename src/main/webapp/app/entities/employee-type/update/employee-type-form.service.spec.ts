import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employee-type.test-samples';

import { EmployeeTypeFormService } from './employee-type-form.service';

describe('EmployeeType Form Service', () => {
  let service: EmployeeTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeeTypeFormService);
  });

  describe('Service methods', () => {
    describe('createEmployeeTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmployeeTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });

      it('passing IEmployeeType should create a new form with FormGroup', () => {
        const formGroup = service.createEmployeeTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });
    });

    describe('getEmployeeType', () => {
      it('should return NewEmployeeType for default EmployeeType initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEmployeeTypeFormGroup(sampleWithNewData);

        const employeeType = service.getEmployeeType(formGroup) as any;

        expect(employeeType).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmployeeType for empty EmployeeType initial value', () => {
        const formGroup = service.createEmployeeTypeFormGroup();

        const employeeType = service.getEmployeeType(formGroup) as any;

        expect(employeeType).toMatchObject({});
      });

      it('should return IEmployeeType', () => {
        const formGroup = service.createEmployeeTypeFormGroup(sampleWithRequiredData);

        const employeeType = service.getEmployeeType(formGroup) as any;

        expect(employeeType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmployeeType should not enable id FormControl', () => {
        const formGroup = service.createEmployeeTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmployeeType should disable id FormControl', () => {
        const formGroup = service.createEmployeeTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
