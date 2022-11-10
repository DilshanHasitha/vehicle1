import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IExpenseAccountBalance } from '../expense-account-balance.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../expense-account-balance.test-samples';

import { ExpenseAccountBalanceService } from './expense-account-balance.service';

const requireRestSample: IExpenseAccountBalance = {
  ...sampleWithRequiredData,
};

describe('ExpenseAccountBalance Service', () => {
  let service: ExpenseAccountBalanceService;
  let httpMock: HttpTestingController;
  let expectedResult: IExpenseAccountBalance | IExpenseAccountBalance[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ExpenseAccountBalanceService);
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

    it('should create a ExpenseAccountBalance', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const expenseAccountBalance = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(expenseAccountBalance).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExpenseAccountBalance', () => {
      const expenseAccountBalance = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(expenseAccountBalance).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExpenseAccountBalance', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExpenseAccountBalance', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ExpenseAccountBalance', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addExpenseAccountBalanceToCollectionIfMissing', () => {
      it('should add a ExpenseAccountBalance to an empty array', () => {
        const expenseAccountBalance: IExpenseAccountBalance = sampleWithRequiredData;
        expectedResult = service.addExpenseAccountBalanceToCollectionIfMissing([], expenseAccountBalance);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(expenseAccountBalance);
      });

      it('should not add a ExpenseAccountBalance to an array that contains it', () => {
        const expenseAccountBalance: IExpenseAccountBalance = sampleWithRequiredData;
        const expenseAccountBalanceCollection: IExpenseAccountBalance[] = [
          {
            ...expenseAccountBalance,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExpenseAccountBalanceToCollectionIfMissing(expenseAccountBalanceCollection, expenseAccountBalance);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExpenseAccountBalance to an array that doesn't contain it", () => {
        const expenseAccountBalance: IExpenseAccountBalance = sampleWithRequiredData;
        const expenseAccountBalanceCollection: IExpenseAccountBalance[] = [sampleWithPartialData];
        expectedResult = service.addExpenseAccountBalanceToCollectionIfMissing(expenseAccountBalanceCollection, expenseAccountBalance);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(expenseAccountBalance);
      });

      it('should add only unique ExpenseAccountBalance to an array', () => {
        const expenseAccountBalanceArray: IExpenseAccountBalance[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const expenseAccountBalanceCollection: IExpenseAccountBalance[] = [sampleWithRequiredData];
        expectedResult = service.addExpenseAccountBalanceToCollectionIfMissing(
          expenseAccountBalanceCollection,
          ...expenseAccountBalanceArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const expenseAccountBalance: IExpenseAccountBalance = sampleWithRequiredData;
        const expenseAccountBalance2: IExpenseAccountBalance = sampleWithPartialData;
        expectedResult = service.addExpenseAccountBalanceToCollectionIfMissing([], expenseAccountBalance, expenseAccountBalance2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(expenseAccountBalance);
        expect(expectedResult).toContain(expenseAccountBalance2);
      });

      it('should accept null and undefined values', () => {
        const expenseAccountBalance: IExpenseAccountBalance = sampleWithRequiredData;
        expectedResult = service.addExpenseAccountBalanceToCollectionIfMissing([], null, expenseAccountBalance, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(expenseAccountBalance);
      });

      it('should return initial array if no ExpenseAccountBalance is added', () => {
        const expenseAccountBalanceCollection: IExpenseAccountBalance[] = [sampleWithRequiredData];
        expectedResult = service.addExpenseAccountBalanceToCollectionIfMissing(expenseAccountBalanceCollection, undefined, null);
        expect(expectedResult).toEqual(expenseAccountBalanceCollection);
      });
    });

    describe('compareExpenseAccountBalance', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExpenseAccountBalance(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareExpenseAccountBalance(entity1, entity2);
        const compareResult2 = service.compareExpenseAccountBalance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareExpenseAccountBalance(entity1, entity2);
        const compareResult2 = service.compareExpenseAccountBalance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareExpenseAccountBalance(entity1, entity2);
        const compareResult2 = service.compareExpenseAccountBalance(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
