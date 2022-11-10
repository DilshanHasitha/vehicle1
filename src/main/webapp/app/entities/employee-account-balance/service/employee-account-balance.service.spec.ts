import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEmployeeAccountBalance } from '../employee-account-balance.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../employee-account-balance.test-samples';

import { EmployeeAccountBalanceService } from './employee-account-balance.service';

const requireRestSample: IEmployeeAccountBalance = {
  ...sampleWithRequiredData,
};

describe('EmployeeAccountBalance Service', () => {
  let service: EmployeeAccountBalanceService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmployeeAccountBalance | IEmployeeAccountBalance[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmployeeAccountBalanceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a EmployeeAccountBalance', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const employeeAccountBalance = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(employeeAccountBalance).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmployeeAccountBalance', () => {
      const employeeAccountBalance = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(employeeAccountBalance).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmployeeAccountBalance', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmployeeAccountBalance', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmployeeAccountBalance', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmployeeAccountBalanceToCollectionIfMissing', () => {
      it('should add a EmployeeAccountBalance to an empty array', () => {
        const employeeAccountBalance: IEmployeeAccountBalance = sampleWithRequiredData;
        expectedResult = service.addEmployeeAccountBalanceToCollectionIfMissing([], employeeAccountBalance);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeAccountBalance);
      });

      it('should not add a EmployeeAccountBalance to an array that contains it', () => {
        const employeeAccountBalance: IEmployeeAccountBalance = sampleWithRequiredData;
        const employeeAccountBalanceCollection: IEmployeeAccountBalance[] = [
          {
            ...employeeAccountBalance,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmployeeAccountBalanceToCollectionIfMissing(employeeAccountBalanceCollection, employeeAccountBalance);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmployeeAccountBalance to an array that doesn't contain it", () => {
        const employeeAccountBalance: IEmployeeAccountBalance = sampleWithRequiredData;
        const employeeAccountBalanceCollection: IEmployeeAccountBalance[] = [sampleWithPartialData];
        expectedResult = service.addEmployeeAccountBalanceToCollectionIfMissing(employeeAccountBalanceCollection, employeeAccountBalance);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeAccountBalance);
      });

      it('should add only unique EmployeeAccountBalance to an array', () => {
        const employeeAccountBalanceArray: IEmployeeAccountBalance[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const employeeAccountBalanceCollection: IEmployeeAccountBalance[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeeAccountBalanceToCollectionIfMissing(
          employeeAccountBalanceCollection,
          ...employeeAccountBalanceArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employeeAccountBalance: IEmployeeAccountBalance = sampleWithRequiredData;
        const employeeAccountBalance2: IEmployeeAccountBalance = sampleWithPartialData;
        expectedResult = service.addEmployeeAccountBalanceToCollectionIfMissing([], employeeAccountBalance, employeeAccountBalance2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeAccountBalance);
        expect(expectedResult).toContain(employeeAccountBalance2);
      });

      it('should accept null and undefined values', () => {
        const employeeAccountBalance: IEmployeeAccountBalance = sampleWithRequiredData;
        expectedResult = service.addEmployeeAccountBalanceToCollectionIfMissing([], null, employeeAccountBalance, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeAccountBalance);
      });

      it('should return initial array if no EmployeeAccountBalance is added', () => {
        const employeeAccountBalanceCollection: IEmployeeAccountBalance[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeeAccountBalanceToCollectionIfMissing(employeeAccountBalanceCollection, undefined, null);
        expect(expectedResult).toEqual(employeeAccountBalanceCollection);
      });
    });

    describe('compareEmployeeAccountBalance', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmployeeAccountBalance(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmployeeAccountBalance(entity1, entity2);
        const compareResult2 = service.compareEmployeeAccountBalance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmployeeAccountBalance(entity1, entity2);
        const compareResult2 = service.compareEmployeeAccountBalance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmployeeAccountBalance(entity1, entity2);
        const compareResult2 = service.compareEmployeeAccountBalance(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
