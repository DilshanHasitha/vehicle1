import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employee.test-samples';

import { EmployeeFormService } from './employee-form.service';

describe('Employee Form Service', () => {
  let service: EmployeeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeeFormService);
  });

  describe('Service methods', () => {
    describe('createEmployeeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmployeeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            email: expect.any(Object),
            isActive: expect.any(Object),
            phone: expect.any(Object),
            addressLine1: expect.any(Object),
            addressLine2: expect.any(Object),
            city: expect.any(Object),
            country: expect.any(Object),
            salary: expect.any(Object),
            user: expect.any(Object),
            type: expect.any(Object),
            vehicles: expect.any(Object),
            merchant: expect.any(Object),
          })
        );
      });

      it('passing IEmployee should create a new form with FormGroup', () => {
        const formGroup = service.createEmployeeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            email: expect.any(Object),
            isActive: expect.any(Object),
            phone: expect.any(Object),
            addressLine1: expect.any(Object),
            addressLine2: expect.any(Object),
            city: expect.any(Object),
            country: expect.any(Object),
            salary: expect.any(Object),
            user: expect.any(Object),
            type: expect.any(Object),
            vehicles: expect.any(Object),
            merchant: expect.any(Object),
          })
        );
      });
    });

    describe('getEmployee', () => {
      it('should return NewEmployee for default Employee initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEmployeeFormGroup(sampleWithNewData);

        const employee = service.getEmployee(formGroup) as any;

        expect(employee).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmployee for empty Employee initial value', () => {
        const formGroup = service.createEmployeeFormGroup();

        const employee = service.getEmployee(formGroup) as any;

        expect(employee).toMatchObject({});
      });

      it('should return IEmployee', () => {
        const formGroup = service.createEmployeeFormGroup(sampleWithRequiredData);

        const employee = service.getEmployee(formGroup) as any;

        expect(employee).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmployee should not enable id FormControl', () => {
        const formGroup = service.createEmployeeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmployee should disable id FormControl', () => {
        const formGroup = service.createEmployeeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
